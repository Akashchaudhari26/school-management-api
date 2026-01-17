package com.sms.modules.exam.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sms.modules.exam.domain.ExamDefinition;

public interface ExamDefinitionRepository extends MongoRepository<ExamDefinition, String> {

    // To fetch list of exams for dropdowns (e.g., show only 2025-2026 exams)
    List<ExamDefinition> findByAcademicYear(String academicYear);

    // To prevent duplicate exam names in the same year
    Optional<ExamDefinition> findByNameAndAcademicYear(String name, String academicYear);
}