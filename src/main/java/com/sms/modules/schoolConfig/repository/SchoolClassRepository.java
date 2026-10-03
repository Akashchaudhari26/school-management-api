package com.sms.modules.schoolConfig.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sms.modules.schoolConfig.domain.SchoolClass;

public interface SchoolClassRepository extends JpaRepository<SchoolClass, String> {
    // Helper to find class by name if ID is random
    Optional<SchoolClass> findByDisplayName(String displayName);
}
