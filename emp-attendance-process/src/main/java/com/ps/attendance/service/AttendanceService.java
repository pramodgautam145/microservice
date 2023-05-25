package com.ps.attendance.service;

import com.ps.attendance.Exception.RecordNotFoundException;
import com.ps.attendance.dto.AttendanceRequest;
import com.ps.attendance.model.Attendance;
import com.ps.attendance.model.EmployeeID;
import com.ps.attendance.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    public Attendance processAttendance(AttendanceRequest request) {
        String status;
        Optional<Attendance> repo = attendanceRepository.findById(new EmployeeID(request.getEmpId(), request.getDate()));
        if (repo.isPresent()) {
            Attendance attendance = repo.get();
            attendance.setStatus(getStatus(attendance));
            return attendance;
        }
        else{
            throw new RecordNotFoundException("Employee Details not Available");
        }



    }

    private String getStatus(Attendance attendance){
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String date = "2023-03-29 18:51:38";
//
//        //convert String to LocalDate
//        LocalDateTime localDate = LocalDateTime.parse(date, formatter);
//        attendance.setSwipeOut(localDate);
        Duration totalDuration = Duration.between(attendance.getSwipeIn(), attendance.getSwipeOut());
        long totalHours = attendance.getTotalHours();

        if (totalHours < 4) {
            return "Absent";
        } else if (totalHours < 8) {
            return "Half day";
        } else {
            return "Present";
        }
    }


    public List<Attendance> findAll(LocalDate date) {
        List<Attendance> list = attendanceRepository.findAll();
        if(null!= list && !list.isEmpty()){
            return list.stream().filter(x->x.getEmployeeID().getDate().equals(date)).collect(Collectors.toList());
        }

        return null;
    }
}
