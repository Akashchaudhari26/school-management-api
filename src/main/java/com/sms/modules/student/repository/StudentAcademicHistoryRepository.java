package com.sms.modules.student.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sms.modules.student.domain.StudentAcademicHistory;

public interface StudentAcademicHistoryRepository extends MongoRepository<StudentAcademicHistory, String> {

}
