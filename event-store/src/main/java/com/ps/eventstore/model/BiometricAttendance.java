package com.ps.eventstore.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.kafka.common.serialization.Serializer;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BiometricAttendance implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long biometricAttendanceId;

    private String type;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime firstSwipeIn;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastSwipeOut;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate date;
    private Long  totalHours;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @OneToMany(mappedBy = "biometricAttendance",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<SwipeAudit> swipeAuditList;
}
