package com.sms.modules.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sms.modules.student.domain.StudentAcademicHistory;

public interface StudentAcademicHistoryRepository extends JpaRepository<StudentAcademicHistory, String> {

}
