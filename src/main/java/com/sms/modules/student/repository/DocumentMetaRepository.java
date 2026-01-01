package com.sms.modules.student.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sms.modules.student.domain.DocumentMeta;

import java.util.List;

public interface DocumentMetaRepository extends MongoRepository<DocumentMeta, String> {
    List<DocumentMeta> findByStudentId(String studentId);
}
