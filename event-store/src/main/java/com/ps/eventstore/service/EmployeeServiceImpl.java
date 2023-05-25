package com.ps.eventstore.service;

import com.ps.eventstore.Exception.EmployeeDetailsNotFoundException;

import com.ps.eventstore.dao.BiometricAttendanceRepository;
import com.ps.eventstore.dao.EmployeeRepository;
import com.ps.eventstore.dao.SwipeAuditRepository;
import com.ps.eventstore.dto.Attendance;
import com.ps.eventstore.dto.AttendanceResponse;
import com.ps.eventstore.dto.EmployeeID;
import com.ps.eventstore.model.BiometricAttendance;
import com.ps.eventstore.model.Employee;
import com.ps.eventstore.model.SwipeAudit;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;


import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SwipeAuditRepository swipeAuditRepository;

    @Autowired
    private BiometricAttendanceRepository biometricAttendanceRepository;

    public static final String EVENT_SERVICE="eventService";


    @Override
    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Attendance getAttendanceStatus(String employeeId, LocalDate date) {
       // RestTemplate restTemplate = new RestTemplate();
        String url = "http://ATTENDANCE-PROCESS-SERVICE/api/process/attendance/{employeeId}/{date}";

        URI uri = UriComponentsBuilder.fromUriString(url)
                .buildAndExpand(employeeId, date)
                .toUri();
        try {
            Attendance response = restTemplate.getForObject(uri, Attendance.class);
            return response;
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.BAD_REQUEST) {
                String responseBody = ex.getResponseBodyAsString();//NOSONAR
                throw new EmployeeDetailsNotFoundException(ex);

            }
        }
        return null;
    }

    @Override
    @CircuitBreaker(name = EVENT_SERVICE,fallbackMethod = "fallBackAttendanceStatus")
    //@Retry(name = EVENT_SERVICE,fallbackMethod = "fallBackAttendanceStatus")
    public List<Attendance> attendanceStatusEmpByDate(LocalDate date) {
       // String url = "http://ATTENDANCE-PROCESS-SERVICE/api/process/findAllAttendance/{date}";
        String url = "http://localhost:8082/api/process/findAllAttendance/{date}";
        URI uri = UriComponentsBuilder.fromUriString(url)
                .buildAndExpand(date)
                .toUri();
        try {
            List<Attendance> response = restTemplate.getForObject(uri, List.class);
            return response;
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.BAD_REQUEST) {
                String responseBody = ex.getResponseBodyAsString();
                throw new EmployeeDetailsNotFoundException(ex);

            }
        }
        return null;

    }

    public List<Attendance> fallBackAttendanceStatus(Exception e){
        return Stream.of(
                new Attendance(new EmployeeID(420L,LocalDate.now()), LocalDateTime.now(),LocalDateTime.now(),2L,"Present")


        ).collect(Collectors.toList());


    }

    @Override
    public List<SwipeAudit> attendanceSwapListUsingEmpIdByDate(Long employeeId, LocalDate date) {
        return swipeAuditRepository.findByEmployee_IdAndDate(employeeId,date);
    }

    @Override
    public AttendanceResponse gatFirstAndLastEmpIdByDate(Long employeeId, LocalDate date) {
        BiometricAttendance respo = biometricAttendanceRepository.findByEmployee_IdAndDate(employeeId, date);
        if(respo !=null){
            AttendanceResponse attendance = new AttendanceResponse();
            attendance.setEmployeeID(respo.getEmployee().getId());
            attendance.setSwipeIn(respo.getFirstSwipeIn());
            attendance.setSwipeOut(respo.getLastSwipeOut());
            attendance.setTotalHours(calculateTotalHours(respo));
            attendance.setStatus(getStatus(attendance));
            return attendance;

        }
        return null;
    }

    private Long calculateTotalHours(BiometricAttendance biometricAttendance) {

        if (biometricAttendance.getLastSwipeOut() != null) {
            Long diff = Duration.between(biometricAttendance.getFirstSwipeIn(), biometricAttendance.getLastSwipeOut()).toHours();
            return diff;
        } else if (biometricAttendance.getLastSwipeOut() == null) {
            return 0l;
        }

        return null;
    }

    private String getStatus(AttendanceResponse attendance){

        long totalHours =   attendance.getTotalHours();

        if (totalHours < 4) {
            return "Absent";
        } else if (totalHours < 8) {
            return "Half day";
        } else {
            return "Present";
        }
    }

}
