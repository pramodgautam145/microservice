package com.ps.eventstore.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class EmployeeID implements Serializable {
    private Long empId;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate date;

    public EmployeeID(){

    }
    public EmployeeID(Long empId, LocalDate date) {
        this.empId = empId;
        this.date = date;
    }

    public Long getEmpId() {
        return empId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmployeeID)) return false;
        EmployeeID that = (EmployeeID) o;
        return Objects.equals(empId, that.empId) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empId, date);
    }
}
