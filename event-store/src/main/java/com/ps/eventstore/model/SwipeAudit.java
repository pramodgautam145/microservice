package com.ps.eventstore.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIgnoreProperties({"employee", "biometricAttendance"})
public class SwipeAudit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long swipeAuditId;
    private LocalDateTime swipeIn;
    private LocalDateTime swipeOut;
    private LocalDate date;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    private BiometricAttendance biometricAttendance;
}
