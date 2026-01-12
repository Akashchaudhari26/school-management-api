package com.sms.modules.schoolConfig.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sms.modules.schoolConfig.domain.AcademicYear;

public interface AcademicYearRepository extends MongoRepository<AcademicYear, String> {
    Optional<AcademicYear> findByIsActiveTrue();
}