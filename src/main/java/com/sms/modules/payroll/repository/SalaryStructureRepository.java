package com.sms.modules.payroll.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sms.modules.payroll.domain.SalaryStructure;

public interface SalaryStructureRepository extends MongoRepository<SalaryStructure, String> {
    Optional<SalaryStructure> findByStaffId(String staffId);
}