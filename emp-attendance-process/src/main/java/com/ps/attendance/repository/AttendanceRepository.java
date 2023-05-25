package com.ps.attendance.repository;

import com.ps.attendance.model.Attendance;

import com.ps.attendance.model.EmployeeID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, EmployeeID> {
}
