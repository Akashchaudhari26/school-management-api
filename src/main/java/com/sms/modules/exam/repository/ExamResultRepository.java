package com.sms.modules.exam.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sms.modules.exam.domain.ExamResult;

import java.util.List;

public interface ExamResultRepository extends MongoRepository<ExamResult, String> {

    // Find marks for a specific "Sheet" (e.g., Class 10-A, Math, Mid-Term)
    List<ExamResult> findByClassIdAndAcademicYearAndExamNameAndSubjectName(String classId, String academicYear,
            String examName, String subjectName);

    List<ExamResult> findByStudentIdAndClassIdAndAcademicYearAndExamName(String studentId, String classId,
            String academicYear, String examName);

    // Get a specific student's report card
    List<ExamResult> findByStudentIdAndAcademicYear(String studentId, String academicYear);

    List<ExamResult> findByClassIdAndAcademicYearAndExamName(String classId, String academicYear, String examName);

}