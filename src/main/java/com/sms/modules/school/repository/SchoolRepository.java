package com.sms.modules.school.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sms.modules.school.domain.School;

@Repository
public interface SchoolRepository extends MongoRepository<School, String> {
    boolean existsBy();
}
