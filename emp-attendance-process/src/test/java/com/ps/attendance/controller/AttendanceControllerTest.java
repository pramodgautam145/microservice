package com.ps.attendance.controller;

import com.ps.attendance.dto.AttendanceRequest;
import com.ps.attendance.model.Attendance;
import com.ps.attendance.model.EmployeeID;
import com.ps.attendance.service.AttendanceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttendanceControllerTest {

    @Mock
    private AttendanceService mockAttendanceService;

    @InjectMocks
    private AttendanceController attendanceControllerUnderTest;

    @Test
    void testGetAttendance1() {
        // Setup
        final Attendance expectedResult = new Attendance(new EmployeeID(1L, LocalDate.of(2020, 1, 1)),
                LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDateTime.of(2020, 1, 1, 0, 0, 0), 0L, "status");

        // Configure AttendanceService.processAttendance(...).
        final Attendance attendance = new Attendance(new EmployeeID(1L, LocalDate.of(2020, 1, 1)),
                LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDateTime.of(2020, 1, 1, 0, 0, 0), 0L, "status");
        when(mockAttendanceService.processAttendance(new AttendanceRequest(1l, LocalDate.of(2020, 1, 1))))
                .thenReturn(attendance);

        // Run the test
        final Attendance result = attendanceControllerUnderTest.getAttendance("1", LocalDate.of(2020, 1, 1));

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    void testGetAttendance2() {
        // Setup
        final List<Attendance> expectedResult = Arrays.asList(
                new Attendance(new EmployeeID(0L, LocalDate.of(2020, 1, 1)), LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0), 0L, "status"));

        // Configure AttendanceService.findAll(...).
        final List<Attendance> attendances = Arrays.asList(
                new Attendance(new EmployeeID(0L, LocalDate.of(2020, 1, 1)), LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0), 0L, "status"));
        when(mockAttendanceService.findAll(LocalDate.of(2020, 1, 1))).thenReturn(attendances);

        // Run the test
        final List<Attendance> result = attendanceControllerUnderTest.getAttendance(LocalDate.of(2020, 1, 1));

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    void testGetAttendance2_AttendanceServiceReturnsNoItems() {
        // Setup
        when(mockAttendanceService.findAll(LocalDate.of(2020, 1, 1))).thenReturn(Collections.emptyList());

        // Run the test
        final List<Attendance> result = attendanceControllerUnderTest.getAttendance(LocalDate.of(2020, 1, 1));

        // Verify the results
        assertEquals(Collections.emptyList(), result);
    }
}
