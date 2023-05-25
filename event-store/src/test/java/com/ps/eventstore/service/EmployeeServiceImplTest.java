package com.ps.eventstore.service;

import com.ps.eventstore.dao.BiometricAttendanceRepository;
import com.ps.eventstore.dao.EmployeeRepository;
import com.ps.eventstore.dao.SwipeAuditRepository;
import com.ps.eventstore.dto.Attendance;
import com.ps.eventstore.dto.AttendanceResponse;
import com.ps.eventstore.dto.EmployeeID;
import com.ps.eventstore.model.BiometricAttendance;
import com.ps.eventstore.model.Employee;
import com.ps.eventstore.model.SwipeAudit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository mockEmployeeRepository;
    @Mock
    private RestTemplate mockRestTemplate;
    @Mock
    private SwipeAuditRepository mockSwipeAuditRepository;
    @Mock
    private BiometricAttendanceRepository mockBiometricAttendanceRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeServiceImplUnderTest;

    @Test
    void testAddEmployee() {
        // Setup
        final Employee employee = new Employee(0L, "firstName", "lastName", "gender", 0L, "address", "title",
                Arrays.asList(new BiometricAttendance(0L, "type", LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDate.of(2020, 1, 1), 0L, null, Arrays.asList(
                        new SwipeAudit(0L, LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                                LocalDate.of(2020, 1, 1), null, null)))), Arrays.asList(
                new SwipeAudit(0L, LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDate.of(2020, 1, 1), null,
                        new BiometricAttendance(0L, "type", LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                                LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDate.of(2020, 1, 1), 0L, null,
                                Arrays.asList()))));

        // Configure EmployeeRepository.save(...).
        final Employee employee1 = new Employee(0L, "firstName", "lastName", "gender", 0L, "address", "title",
                Arrays.asList(new BiometricAttendance(0L, "type", LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDate.of(2020, 1, 1), 0L, null, Arrays.asList(
                        new SwipeAudit(0L, LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                                LocalDate.of(2020, 1, 1), null, null)))), Arrays.asList(
                new SwipeAudit(0L, LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDate.of(2020, 1, 1), null,
                        new BiometricAttendance(0L, "type", LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                                LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDate.of(2020, 1, 1), 0L, null,
                                Arrays.asList()))));
        when(mockEmployeeRepository.save(any(Employee.class))).thenReturn(employee1);

        // Run the test
        final Employee result = employeeServiceImplUnderTest.addEmployee(employee);

        assertNotNull(result);
    }

    @Test
    void testAttendanceStatusEmpByDate_RestTemplateReturnsNull() throws Exception {
        // Setup
        List<Attendance> response = new ArrayList<>();
        Attendance attendance = new Attendance();
        response.add(attendance);
        when(mockRestTemplate.getForObject(any(),any())).thenReturn(null);

        // Run the test
        final List<Attendance> result = employeeServiceImplUnderTest.attendanceStatusEmpByDate(
                LocalDate.of(2020, 1, 1));

        // Verify the results
        assertNull(result);
    }





    @Test
    void testAttendanceSwapListUsingEmpIdByDate() {
        // Setup
        // Configure SwipeAuditRepository.findByEmployee_IdAndDate(...).
        final List<SwipeAudit> swipeAudits = Arrays.asList(
                new SwipeAudit(0L, LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDate.of(2020, 1, 1),
                        new Employee(0L, "firstName", "lastName", "gender", 0L, "address", "title", Arrays.asList(
                                new BiometricAttendance(0L, "type", LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                                        LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDate.of(2020, 1, 1), 0L, null,
                                        Arrays.asList())), Arrays.asList()),
                        new BiometricAttendance(0L, "type", LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                                LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDate.of(2020, 1, 1), 0L,
                                new Employee(0L, "firstName", "lastName", "gender", 0L, "address", "title",
                                        Arrays.asList(), Arrays.asList()), Arrays.asList())));
        when(mockSwipeAuditRepository.findByEmployee_IdAndDate(0L, LocalDate.of(2020, 1, 1))).thenReturn(swipeAudits);

        // Run the test
        final List<SwipeAudit> result = employeeServiceImplUnderTest.attendanceSwapListUsingEmpIdByDate(0L,
                LocalDate.of(2020, 1, 1));

        assertNotNull(result);
    }

    @Test
    void testAttendanceSwapListUsingEmpIdByDate_SwipeAuditRepositoryReturnsNoItems() {
        // Setup
        when(mockSwipeAuditRepository.findByEmployee_IdAndDate(0L, LocalDate.of(2020, 1, 1)))
                .thenReturn(Collections.emptyList());

        // Run the test
        final List<SwipeAudit> result = employeeServiceImplUnderTest.attendanceSwapListUsingEmpIdByDate(0L,
                LocalDate.of(2020, 1, 1));

        // Verify the results
        assertEquals(Collections.emptyList(), result);
    }



    @Test
    void testGatFirstAndLastEmpIdByDate_BiometricAttendanceRepositoryReturnsNull() {
        // Setup
        when(mockBiometricAttendanceRepository.findByEmployee_IdAndDate(0L, LocalDate.of(2020, 1, 1))).thenReturn(null);

        // Run the test
        final AttendanceResponse result = employeeServiceImplUnderTest.gatFirstAndLastEmpIdByDate(0L,
                LocalDate.of(2020, 1, 1));

        // Verify the results
        assertNull(result);
    }
}
