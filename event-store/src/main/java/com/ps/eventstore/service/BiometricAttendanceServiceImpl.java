package com.ps.eventstore.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ps.eventstore.Exception.RecordNotFoundException;
import com.ps.eventstore.dao.BiometricAttendanceRepository;
import com.ps.eventstore.dao.SwipeAuditRepository;
import com.ps.eventstore.model.BiometricAttendance;
import com.ps.eventstore.model.SwipeAudit;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class BiometricAttendanceServiceImpl implements BiometricAttendanceService {

    public static final String TOPIC_NAME = "ATTENDANCE_EVENT";
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private BiometricAttendanceRepository biometricAttendanceRepository;
    @Autowired
    private SwipeAuditRepository swipeAuditRepository;
    private static final String SWAP_OUT = "OUT";
    private static final String SWAP_IN = "IN";

    @SneakyThrows
    @Override
    public BiometricAttendance swipeInAndOut(BiometricAttendance biometricAttendance) {

        log.info("Request received swap in and out:: "+biometricAttendance);
        BiometricAttendance biometricAttendanceObject = calculateWorkingHoursInDay(biometricAttendance);
//        if (SWAP_OUT.equalsIgnoreCase(biometricAttendance.getType())) {
//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.registerModule(new JavaTimeModule());
//            String jsonString = objectMapper.writeValueAsString(biometricAttendanceObject);
//
//            kafkaTemplate.send(TOPIC_NAME, jsonString);
//        }
        return biometricAttendance;
    }

    @Scheduled(cron = "${cron.expression.val}")
    private void endOfDayBatchJob() throws JsonProcessingException {
        log.info("----Job Started for sending the one day data of an employee---");
        List<BiometricAttendance> attendanceList = getListBiometricAttendence();
        for (BiometricAttendance biometricAttendanceObject:attendanceList) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String jsonString = objectMapper.writeValueAsString(biometricAttendanceObject);
            kafkaTemplate.send(TOPIC_NAME, jsonString);
        }
    }

    private List<BiometricAttendance> getListBiometricAttendence(){
        List<BiometricAttendance> list = biometricAttendanceRepository.findAll();
        if(!list.isEmpty() && list.size()>0){
            return list;
        }
       return null;//NOSONAR
    }

    private boolean validateSwipeIn(BiometricAttendance biometricAttendance, String type) {
        if (SWAP_OUT.equalsIgnoreCase(type)) {
            if (biometricAttendance != null && biometricAttendance.getFirstSwipeIn() != null) {
                if (SWAP_OUT.equalsIgnoreCase(biometricAttendance.getType())) {
                    throw new RecordNotFoundException("before swap-out please do swap-in");//NOSONAR
                }
                return true;

            } else {
                return false;
            }
        } else if (SWAP_IN.equalsIgnoreCase(type)) {
            if (biometricAttendance != null && biometricAttendance.getFirstSwipeIn() != null && "In".equalsIgnoreCase(biometricAttendance.getType())) {
                throw new RecordNotFoundException("before swap-out please do swap-in");
            }
            return true;
        }
        return true;
    }

    private BiometricAttendance calculateWorkingHoursInDay(BiometricAttendance biometricAttendance) {

        boolean valid = false;
        BiometricAttendance biometricAttendanceDb = biometricAttendanceRepository.findByEmployee_IdAndDate(biometricAttendance.getEmployee().getId(), LocalDate.now());
        valid = validateSwipeIn(biometricAttendanceDb, biometricAttendance.getType());
        if (valid) {

            BiometricAttendance repo = prepareBiometricAttendanceExisting(biometricAttendance, biometricAttendanceDb);
            calculateHoursInsideOfficeForADay(repo);
            biometricAttendanceRepository.save(repo);
            return repo;

        } else {
            //To do exception
            throw new RecordNotFoundException("before swap-out please do swap-in");
        }


    }

    public BiometricAttendance prepareBiometricAttendanceExisting(BiometricAttendance biometricAttendance, BiometricAttendance biometricAttendanceDb) //NOSONAR
    {
        if (biometricAttendanceDb != null) {
            if (SWAP_OUT.equalsIgnoreCase(biometricAttendance.getType())) {
                biometricAttendanceDb.setLastSwipeOut(LocalDateTime.now());
            }
            if (SWAP_IN.equalsIgnoreCase(biometricAttendance.getType())) {
                if (biometricAttendanceDb.getFirstSwipeIn() == null)//NOSONAR
                    biometricAttendanceDb.setFirstSwipeIn(LocalDateTime.now());
            }
            //biometricAttendanceDb.setLastSwipeOut(LocalDateTime.now());
            biometricAttendanceDb.setType(biometricAttendance.getType());
            calculateTotalHours(biometricAttendanceDb);
            biometricAttendanceDb.setDate(LocalDate.now());
            //biometricAttendanceDb.setEmployee(biometricAttendance.getEmployee());
            prePareAuditing(biometricAttendanceDb);
            return biometricAttendanceDb;
        } else {
            if (SWAP_OUT.equalsIgnoreCase(biometricAttendance.getType())) {
                biometricAttendance.setLastSwipeOut(LocalDateTime.now());
            }
            if (SWAP_IN.equalsIgnoreCase(biometricAttendance.getType())) {
                if (biometricAttendance.getFirstSwipeIn() == null)//NOSONAR
                    biometricAttendance.setFirstSwipeIn(LocalDateTime.now());
            }
            biometricAttendance.setDate(LocalDate.now());
            biometricAttendance.setType(biometricAttendance.getType());
            calculateTotalHours(biometricAttendance);
            biometricAttendance.setDate(LocalDate.now());
            biometricAttendance.setEmployee(biometricAttendance.getEmployee());
            prePareAuditing(biometricAttendance);
            return biometricAttendance;
        }

    }

    private void prePareAuditing(BiometricAttendance biometricAttendance) //NOSONAR
    {
        List<SwipeAudit> list = new ArrayList<>();
        if (biometricAttendance.getSwipeAuditList() != null && biometricAttendance.getSwipeAuditList().size() >= 1) {
            List<SwipeAudit> existingAuditList = biometricAttendance.getSwipeAuditList();
            boolean valid = false;

            for (SwipeAudit swipeAudit : existingAuditList) {
                if (swipeAudit.getSwipeIn() != null && swipeAudit.getSwipeOut() == null) {
                    swipeAudit.setSwipeOut(LocalDateTime.now());
                    list.add(swipeAudit);
                    biometricAttendance.setSwipeAuditList(list);
                    valid = true;
                    break;
                }

            }
            if (!valid) {
                SwipeAudit swipeAudit1 = new SwipeAudit();
                swipeAudit1.setBiometricAttendance(biometricAttendance);
                if (SWAP_IN.equalsIgnoreCase(biometricAttendance.getType()))
                    swipeAudit1.setSwipeIn(LocalDateTime.now());
                if (SWAP_OUT.equalsIgnoreCase(biometricAttendance.getType()))
                    swipeAudit1.setSwipeOut(LocalDateTime.now());
                swipeAudit1.setDate(LocalDate.now());
                swipeAudit1.setEmployee(biometricAttendance.getEmployee());
                list.add(swipeAudit1);
                biometricAttendance.setSwipeAuditList(list);
            }

        } else {
            SwipeAudit swipeAudit = new SwipeAudit();
            swipeAudit.setBiometricAttendance(biometricAttendance);
            if (SWAP_IN.equalsIgnoreCase(biometricAttendance.getType()))
                swipeAudit.setSwipeIn(LocalDateTime.now());
            if (SWAP_OUT.equalsIgnoreCase(biometricAttendance.getType()))
                swipeAudit.setSwipeOut(LocalDateTime.now());
            swipeAudit.setDate(LocalDate.now());
            swipeAudit.setEmployee(biometricAttendance.getEmployee());
            list.add(swipeAudit);
            biometricAttendance.setSwipeAuditList(list);
        }


    }

    private BiometricAttendance calculateTotalHours(BiometricAttendance biometricAttendance) {

        if (biometricAttendance.getLastSwipeOut() != null) {
            Long diff = Duration.between(biometricAttendance.getFirstSwipeIn(), biometricAttendance.getLastSwipeOut()).toHours();
            biometricAttendance.setTotalHours(diff);
        } else if (biometricAttendance.getLastSwipeOut() == null) {
            biometricAttendance.setTotalHours(0L);
        }

        return biometricAttendance;
    }

    private void calculateHoursInsideOfficeForADay(BiometricAttendance biometricAttendance) {
        List<SwipeAudit> swipeAudits = swipeAuditRepository.findByEmployee_IdAndDate(biometricAttendance.getEmployee().getId(), LocalDate.now());
       // List<SwipeAudit> swipeAudits = biometricAttendance.getSwipeAuditList();
        if (!swipeAudits.isEmpty()) {
            if (SWAP_OUT.equalsIgnoreCase(biometricAttendance.getType())) {
                long sumHours = 0;
                for (SwipeAudit swipeAudit : swipeAudits) {
                    LocalDateTime inTime = swipeAudit.getSwipeIn();
                    LocalDateTime outTime = swipeAudit.getSwipeOut();
                    if (inTime != null && outTime != null) {
                        Duration duration = Duration.between(inTime, outTime);
                        long seconds = duration.getSeconds();
                        sumHours = sumHours + seconds;

                    }
                }
                long seconds = sumHours;
                double hours = (double) seconds / 3600;
                System.out.println("Hours: " + hours);
                log.info("total hours calculate ::"+hours);
                biometricAttendance.setTotalHours(Math.round(hours));
            }
        }
    }
}
