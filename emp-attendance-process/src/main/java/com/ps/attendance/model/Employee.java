package com.ps.attendance.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Employee {


    private Long id;
    private String firstName;
    private String lastName;
    private String gender;
    private Long mobileNo;
    private String address;
    private String title;


    private List<BiometricAttendance> biometricAttendances;


    private List<SwipeAudit> swipeAuditList;

}
