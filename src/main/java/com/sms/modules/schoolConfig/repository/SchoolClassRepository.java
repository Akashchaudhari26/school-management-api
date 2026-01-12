package com.sms.modules.schoolConfig.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sms.modules.schoolConfig.domain.SchoolClass;

public interface SchoolClassRepository extends MongoRepository<SchoolClass, String> {
    // Helper to find class by name if ID is random
    Optional<SchoolClass> findByDisplayName(String displayName);
}
