package com.ps.attendance.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@JsonIgnoreProperties({"employee", "biometricAttendance"})
public class SwipeAudit {

    private Long swipeAuditId;
    private LocalDateTime swipeIn;
    private LocalDateTime swipeOut;
    private LocalDate date;

    Employee employee;

    private BiometricAttendance biometricAttendance;
}
