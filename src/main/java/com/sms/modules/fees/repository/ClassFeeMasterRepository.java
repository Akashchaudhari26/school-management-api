package com.sms.modules.fees.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sms.modules.fees.domain.ClassFeeMaster;

public interface ClassFeeMasterRepository extends MongoRepository<ClassFeeMaster, String> {
    // Find the template for a specific class and year
    Optional<ClassFeeMaster> findByClassIdAndAcademicYear(String classId, String academicYear);

    List<ClassFeeMaster> findByAcademicYear(String academicYear);
}