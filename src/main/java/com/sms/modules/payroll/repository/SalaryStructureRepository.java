package com.sms.modules.payroll.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sms.modules.payroll.domain.SalaryStructure;

public interface SalaryStructureRepository extends JpaRepository<SalaryStructure, String> {
    Optional<SalaryStructure> findByStaffId(String staffId);
}