package com.sms.modules.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sms.modules.student.domain.DocumentMeta;

import java.util.List;

public interface DocumentMetaRepository extends JpaRepository<DocumentMeta, String> {
    List<DocumentMeta> findByStudentId(String studentId);
}
