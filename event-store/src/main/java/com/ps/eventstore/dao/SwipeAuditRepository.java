package com.ps.eventstore.dao;

import com.ps.eventstore.model.SwipeAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import java.util.List;

@Repository
public interface SwipeAuditRepository extends JpaRepository<SwipeAudit,Long> {

    List<SwipeAudit> findByEmployee_IdAndDate(Long empId, LocalDate startDate1);
}