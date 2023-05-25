package com.ps.attendance.controller;

import com.ps.attendance.dto.AttendanceRequest;

import com.ps.attendance.model.Attendance;

import com.ps.attendance.service.AttendanceService;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/process")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/attendance/{employeeId}/{date}")
    public Attendance getAttendance(@PathVariable String employeeId, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return attendanceService.processAttendance(new AttendanceRequest(Long.parseLong(employeeId),date));
    }

    @Timed(value ="attendance-details")
    @GetMapping("/findAllAttendance/{date}")
    public List<Attendance> getAttendance(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return attendanceService.findAll(date);
    }
}
