package com.ps.eventstore.service;

import com.ps.eventstore.dto.Attendance;
import com.ps.eventstore.dto.AttendanceResponse;
import com.ps.eventstore.model.Employee;
import com.ps.eventstore.model.SwipeAudit;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeService {

    Employee addEmployee(Employee employee);

    Attendance getAttendanceStatus(String employeeId, LocalDate date);

    List<Attendance> attendanceStatusEmpByDate(LocalDate date);


    List<SwipeAudit> attendanceSwapListUsingEmpIdByDate(Long employeeId, LocalDate date);

    AttendanceResponse gatFirstAndLastEmpIdByDate(Long employeeId, LocalDate date);
}
