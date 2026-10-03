package com.sms.modules.schoolConfig.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sms.modules.schoolConfig.domain.Subject;

public interface SubjectRepository extends JpaRepository<Subject, String> {
}
