package com.sms.modules.schoolConfig.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sms.modules.schoolConfig.domain.AcademicYear;

public interface AcademicYearRepository extends JpaRepository<AcademicYear, String> {
    Optional<AcademicYear> findByIsActiveTrue();
}