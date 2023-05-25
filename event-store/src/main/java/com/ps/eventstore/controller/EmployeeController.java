package com.ps.eventstore.controller;

import com.ps.eventstore.dto.Attendance;
import com.ps.eventstore.dto.AttendanceResponse;
import com.ps.eventstore.model.BiometricAttendance;
import com.ps.eventstore.model.Employee;
import com.ps.eventstore.model.SwipeAudit;
import com.ps.eventstore.service.BiometricAttendanceService;
import com.ps.eventstore.service.EmployeeService;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private BiometricAttendanceService biometricAttendanceService;

    @Timed(value ="event-register")
    @PostMapping("/register")
    public ResponseEntity<Employee> registerEmployee(@RequestBody Employee employee) //NOSONAR
    {
        return new ResponseEntity<>(employeeService.addEmployee(employee), HttpStatus.CREATED);
    }

    @PostMapping("/swipe")
    public ResponseEntity<String> swipeInAndOut(@RequestBody BiometricAttendance biometricAttendance)//NOSONAR
    {
        log.info("Request received swap in and out at Controller:: "+biometricAttendance);
        String message;
        BiometricAttendance response = biometricAttendanceService.swipeInAndOut(biometricAttendance);

        if(null != response)
          message  = "Swipe attendance Successfully done";
        else
            message  = "Error Occurred";
        log.info("Response received swap in and out at Controller:: "+message);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/attendance-Status/{employeeId}/{date}")
    public ResponseEntity<Attendance> statusAttendanceEmployee(@PathVariable String employeeId, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return new ResponseEntity<>(employeeService.getAttendanceStatus(employeeId,date), HttpStatus.OK);
    }

    @GetMapping("/attendance-Status/{date}")
    public ResponseEntity<List<Attendance>> attendanceStatusEmpByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return new ResponseEntity<>(employeeService.attendanceStatusEmpByDate(date), HttpStatus.OK);
    }

    @GetMapping("/findAll-swap/{employeeId}/{date}")
    public ResponseEntity<List<SwipeAudit>> attendanceSwapListUsingEmpIdByDate(@PathVariable Long employeeId, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return new ResponseEntity<>(employeeService.attendanceSwapListUsingEmpIdByDate(employeeId,date), HttpStatus.OK);
    }

    @GetMapping("/total-hours-first-swap-in-last-out/{employeeId}/{date}")
    public ResponseEntity<AttendanceResponse> gatFirstAndLastEmpIdByDate(@PathVariable Long employeeId, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return new ResponseEntity<>(employeeService.gatFirstAndLastEmpIdByDate(employeeId,date), HttpStatus.OK);
    }
}
