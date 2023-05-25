package com.ps.eventstore.dao;

import com.ps.eventstore.model.BiometricAttendance;
import com.ps.eventstore.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BiometricAttendanceRepository extends JpaRepository<BiometricAttendance,Long> {

   BiometricAttendance findByEmployee_IdAndDate(Long empId, LocalDate date);

}
