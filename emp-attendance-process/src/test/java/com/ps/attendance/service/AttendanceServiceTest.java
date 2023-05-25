package com.ps.attendance.service;

import com.ps.attendance.Exception.RecordNotFoundException;
import com.ps.attendance.dto.AttendanceRequest;
import com.ps.attendance.model.Attendance;
import com.ps.attendance.model.EmployeeID;
import com.ps.attendance.repository.AttendanceRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {

    @Mock
    private AttendanceRepository mockAttendanceRepository;

    @InjectMocks
    private AttendanceService attendanceServiceUnderTest;

    @Test
    void testProcessAttendance() {
        // Setup
        final AttendanceRequest request = new AttendanceRequest(0L, LocalDate.of(2020, 1, 1));
        final Attendance expectedResult = new Attendance(new EmployeeID(0L, LocalDate.of(2020, 1, 1)),
                LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDateTime.of(2020, 1, 1, 0, 0, 0), 0L, "Absent");

        // Configure AttendanceRepository.findById(...).
        final Optional<Attendance> attendance = Optional.of(
                new Attendance(new EmployeeID(0L, LocalDate.of(2020, 1, 1)), LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0), 0L, "status"));
        when(mockAttendanceRepository.findById(new EmployeeID(0L, LocalDate.of(2020, 1, 1)))).thenReturn(attendance);

        // Run the test
        final Attendance result = attendanceServiceUnderTest.processAttendance(request);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    void testProcessAttendance_AttendanceRepositoryReturnsAbsent() {
        // Setup
        final AttendanceRequest request = new AttendanceRequest(0L, LocalDate.of(2020, 1, 1));
        when(mockAttendanceRepository.findById(new EmployeeID(0L, LocalDate.of(2020, 1, 1))))
                .thenReturn(Optional.empty());

        // Run the test
        assertThrows(RecordNotFoundException.class, () -> attendanceServiceUnderTest.processAttendance(request));
    }

    @Test
    void testFindAll() {
        // Setup
        final List<Attendance> expectedResult = Arrays.asList(
                new Attendance(new EmployeeID(0L, LocalDate.of(2020, 1, 1)), LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0), 0L, "status"));

        // Configure AttendanceRepository.findAll(...).
        final List<Attendance> attendances = Arrays.asList(
                new Attendance(new EmployeeID(0L, LocalDate.of(2020, 1, 1)), LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0), 0L, "status"));
        when(mockAttendanceRepository.findAll()).thenReturn(attendances);

        // Run the test
        final List<Attendance> result = attendanceServiceUnderTest.findAll(LocalDate.of(2020, 1, 1));

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    void testFindAll_AttendanceRepositoryReturnsNull() {
        // Setup
        when(mockAttendanceRepository.findAll()).thenReturn(null);

        // Run the test
        final List<Attendance> result = attendanceServiceUnderTest.findAll(LocalDate.of(2020, 1, 1));

        // Verify the results
        assertNull(result);
    }

    @Test
    void testFindAll_AttendanceRepositoryReturnsNoItems() {
        // Setup
        when(mockAttendanceRepository.findAll()).thenReturn(Collections.emptyList());

        // Run the test
        final List<Attendance> result = attendanceServiceUnderTest.findAll(LocalDate.of(2020, 1, 1));

        // Verify the results
        assertNull(result);
    }
}
