package com.ps.attendance.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ps.attendance.model.Attendance;
import com.ps.attendance.model.BiometricAttendance;
import com.ps.attendance.model.EmployeeID;
import com.ps.attendance.repository.AttendanceRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;




@Service
public class AttendanceEventListener {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @SneakyThrows
    @KafkaListener(topics = "ATTENDANCE_EVENT",groupId = "group_id")
    public void processAttendanceEvent(String attendanceEvent) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        BiometricAttendance biometricAttendance = mapper.readValue(attendanceEvent, BiometricAttendance.class);
        Attendance attendanceSave =saveAttendance(biometricAttendance);
         attendanceRepository.save(attendanceSave);
        System.out.println("listener---"+attendanceEvent);

    }

    private Attendance saveAttendance(BiometricAttendance biometricAttendance){
        Attendance attendance = new Attendance();
        attendance.setEmployeeID(new EmployeeID(biometricAttendance.getEmployee().getId(),biometricAttendance.getDate()));
        attendance.setSwipeIn(biometricAttendance.getFirstSwipeIn());
        attendance.setSwipeOut(biometricAttendance.getLastSwipeOut());
        attendance.setTotalHours(biometricAttendance.getTotalHours());
        attendance.setStatus(getStatus(attendance));

        return attendance;

    }

    private String getStatus(Attendance attendance){

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
