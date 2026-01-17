package com.sms.modules.fees.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sms.modules.fees.domain.ClassFeeMaster;

public interface ClassFeeMasterRepository extends MongoRepository<ClassFeeMaster, String> {
    // Find the template for a specific class and year
    Optional<ClassFeeMaster> findByClassIdAndAcademicYear(String classId, String academicYear);
    
    // 2. Fallback: Get the latest defined structure for this class (Priority 2)
    // "Top" means the first result, "Desc" means newest year first (e.g., 2024
    // before 2023)
    Optional<ClassFeeMaster> findTopByClassIdOrderByAcademicYearDesc(String classId);

    List<ClassFeeMaster> findByAcademicYear(String academicYear);
}