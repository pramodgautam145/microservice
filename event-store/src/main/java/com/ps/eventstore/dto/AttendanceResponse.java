package com.ps.eventstore.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Data
public class AttendanceResponse {

   private Long employeeID;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime swipeIn;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime swipeOut;


    private Long  totalHours;

    private String status;
}
