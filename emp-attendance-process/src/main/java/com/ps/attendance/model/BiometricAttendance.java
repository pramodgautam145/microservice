package com.ps.attendance.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor

public class BiometricAttendance {


    private Long biometricAttendanceId;

    private String type;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime firstSwipeIn;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastSwipeOut;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate date;
    private Long  totalHours;


    private Employee employee;


    private List<SwipeAudit> swipeAuditList;

 public BiometricAttendance() {

 }


 public BiometricAttendance(String json) throws JsonProcessingException {
  ObjectMapper objectMapper = new ObjectMapper();
  objectMapper.registerModule(new JavaTimeModule());
  BiometricAttendance biometricAttendance = objectMapper.readValue(json, BiometricAttendance.class);

  this.biometricAttendanceId = biometricAttendance.biometricAttendanceId;
  this.type = biometricAttendance.type;
  this.firstSwipeIn = biometricAttendance.firstSwipeIn;
  this.lastSwipeOut = biometricAttendance.lastSwipeOut;
  this.date = biometricAttendance.date;
  this.totalHours = biometricAttendance.totalHours;
  this.employee = biometricAttendance.employee;
  this.swipeAuditList = biometricAttendance.swipeAuditList;
 }
}
