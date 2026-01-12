package com.sms.modules.schoolConfig.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sms.modules.schoolConfig.domain.Subject;

public interface SubjectRepository extends MongoRepository<Subject, String> {
}
