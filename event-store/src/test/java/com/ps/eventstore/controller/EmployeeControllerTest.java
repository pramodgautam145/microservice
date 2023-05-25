package com.ps.eventstore.controller;

import com.ps.eventstore.dto.Attendance;
import com.ps.eventstore.dto.AttendanceResponse;
import com.ps.eventstore.dto.EmployeeID;
import com.ps.eventstore.model.BiometricAttendance;
import com.ps.eventstore.model.Employee;
import com.ps.eventstore.model.SwipeAudit;
import com.ps.eventstore.service.BiometricAttendanceService;
import com.ps.eventstore.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeService mockEmployeeService;
    @Mock
    private BiometricAttendanceService mockBiometricAttendanceService;

    @InjectMocks
    private EmployeeController employeeControllerUnderTest;

    @Test
    void testRegisterEmployee() {
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

        // Configure EmployeeService.addEmployee(...).
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
        when(mockEmployeeService.addEmployee(any(Employee.class))).thenReturn(employee1);

        // Run the test
        final ResponseEntity<Employee> result = employeeControllerUnderTest.registerEmployee(employee);
        assertNotNull(result);
        // Verify the results
    }

    @Test
    void testSwipeInAndOut() {
        // Setup
        final BiometricAttendance biometricAttendance = new BiometricAttendance(0L, "type",
                LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDate.of(2020, 1, 1),
                0L, new Employee(0L, "firstName", "lastName", "gender", 0L, "address", "title", Arrays.asList(),
                Arrays.asList(
                        new SwipeAudit(0L, LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                                LocalDate.of(2020, 1, 1), null, null))), Arrays.asList(
                new SwipeAudit(0L, LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDate.of(2020, 1, 1),
                        new Employee(0L, "firstName", "lastName", "gender", 0L, "address", "title", Arrays.asList(),
                                Arrays.asList()), null)));
        final ResponseEntity<String> expectedResult = new ResponseEntity<>("Swipe attendance Successfully done", HttpStatus.CREATED);

        // Configure BiometricAttendanceService.swipeInAndOut(...).
        final BiometricAttendance biometricAttendance1 = new BiometricAttendance(0L, "type",
                LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDate.of(2020, 1, 1),
                0L, new Employee(0L, "firstName", "lastName", "gender", 0L, "address", "title", Arrays.asList(),
                Arrays.asList(
                        new SwipeAudit(0L, LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                                LocalDate.of(2020, 1, 1), null, null))), Arrays.asList(
                new SwipeAudit(0L, LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDate.of(2020, 1, 1),
                        new Employee(0L, "firstName", "lastName", "gender", 0L, "address", "title", Arrays.asList(),
                                Arrays.asList()), null)));
        when(mockBiometricAttendanceService.swipeInAndOut(any(BiometricAttendance.class)))
                .thenReturn(biometricAttendance1);

        // Run the test
        final ResponseEntity<String> result = employeeControllerUnderTest.swipeInAndOut(biometricAttendance);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    void testSwipeInAndOut_BiometricAttendanceServiceReturnsNull() {
        // Setup
        final BiometricAttendance biometricAttendance = new BiometricAttendance(0L, "type",
                LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDate.of(2020, 1, 1),
                0L, new Employee(0L, "firstName", "lastName", "gender", 0L, "address", "title", Arrays.asList(),
                Arrays.asList(
                        new SwipeAudit(0L, LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                                LocalDate.of(2020, 1, 1), null, null))), Arrays.asList(
                new SwipeAudit(0L, LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDate.of(2020, 1, 1),
                        new Employee(0L, "firstName", "lastName", "gender", 0L, "address", "title", Arrays.asList(),
                                Arrays.asList()), null)));
        final ResponseEntity<String> expectedResult = new ResponseEntity<>("Error Occurred", HttpStatus.CREATED);
        when(mockBiometricAttendanceService.swipeInAndOut(any(BiometricAttendance.class))).thenReturn(null);

        // Run the test
        final ResponseEntity<String> result = employeeControllerUnderTest.swipeInAndOut(biometricAttendance);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    void testStatusAttendanceEmployee() {
        // Setup
        final ResponseEntity<Attendance> expectedResult = new ResponseEntity<>(
                new Attendance(new EmployeeID(0L, LocalDate.of(2020, 1, 1)), LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0), 0L, "status"), HttpStatus.OK);

        // Configure EmployeeService.getAttendanceStatus(...).
        final Attendance attendance = new Attendance(new EmployeeID(0L, LocalDate.of(2020, 1, 1)),
                LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDateTime.of(2020, 1, 1, 0, 0, 0), 0L, "status");
        when(mockEmployeeService.getAttendanceStatus("employeeId", LocalDate.of(2020, 1, 1))).thenReturn(attendance);

        // Run the test
        final ResponseEntity<Attendance> result = employeeControllerUnderTest.statusAttendanceEmployee("employeeId",
                LocalDate.of(2020, 1, 1));

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    void testAttendanceStatusEmpByDate() {
        // Setup
        final ResponseEntity<List<Attendance>> expectedResult = new ResponseEntity<>(Arrays.asList(
                new Attendance(new EmployeeID(0L, LocalDate.of(2020, 1, 1)), LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0), 0L, "status")), HttpStatus.OK);

        // Configure EmployeeService.attendanceStatusEmpByDate(...).
        final List<Attendance> attendances = Arrays.asList(
                new Attendance(new EmployeeID(0L, LocalDate.of(2020, 1, 1)), LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0), 0L, "status"));
        when(mockEmployeeService.attendanceStatusEmpByDate(LocalDate.of(2020, 1, 1))).thenReturn(attendances);

        // Run the test
        final ResponseEntity<List<Attendance>> result = employeeControllerUnderTest.attendanceStatusEmpByDate(
                LocalDate.of(2020, 1, 1));

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    void testAttendanceStatusEmpByDate_EmployeeServiceReturnsNoItems() {
        // Setup
        when(mockEmployeeService.attendanceStatusEmpByDate(LocalDate.of(2020, 1, 1)))
                .thenReturn(Collections.emptyList());

        // Run the test
        final ResponseEntity<List<Attendance>> result = employeeControllerUnderTest.attendanceStatusEmpByDate(
                LocalDate.of(2020, 1, 1));

        // Verify the results
        assertEquals(ResponseEntity.ok(Collections.emptyList()), result);
    }

    @Test
    void testAttendanceSwapListUsingEmpIdByDate() {
        // Setup
        // Configure EmployeeService.attendanceSwapListUsingEmpIdByDate(...).
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
        when(mockEmployeeService.attendanceSwapListUsingEmpIdByDate(0L, LocalDate.of(2020, 1, 1)))
                .thenReturn(swipeAudits);

        // Run the test
        final ResponseEntity<List<SwipeAudit>> result = employeeControllerUnderTest.attendanceSwapListUsingEmpIdByDate(
                0L, LocalDate.of(2020, 1, 1));
        assertNotNull(result);
        // Verify the results
    }

    @Test
    void testAttendanceSwapListUsingEmpIdByDate_EmployeeServiceReturnsNoItems() {
        // Setup
        when(mockEmployeeService.attendanceSwapListUsingEmpIdByDate(0L, LocalDate.of(2020, 1, 1)))
                .thenReturn(Collections.emptyList());

        // Run the test
        final ResponseEntity<List<SwipeAudit>> result = employeeControllerUnderTest.attendanceSwapListUsingEmpIdByDate(
                0L, LocalDate.of(2020, 1, 1));

        // Verify the results
        assertEquals(ResponseEntity.ok(Collections.emptyList()), result);
    }

    @Test
    void testGatFirstAndLastEmpIdByDate() {
        // Setup
        final ResponseEntity<AttendanceResponse> expectedResult = new ResponseEntity<>(
                new AttendanceResponse(0L, LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        0L, "status"), HttpStatus.OK);

        // Configure EmployeeService.gatFirstAndLastEmpIdByDate(...).
        final AttendanceResponse attendanceResponse = new AttendanceResponse(0L, LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                LocalDateTime.of(2020, 1, 1, 0, 0, 0), 0L, "status");
        when(mockEmployeeService.gatFirstAndLastEmpIdByDate(0L, LocalDate.of(2020, 1, 1)))
                .thenReturn(attendanceResponse);

        // Run the test
        final ResponseEntity<AttendanceResponse> result = employeeControllerUnderTest.gatFirstAndLastEmpIdByDate(0L,
                LocalDate.of(2020, 1, 1));

        // Verify the results
        assertEquals(expectedResult, result);
    }
}
