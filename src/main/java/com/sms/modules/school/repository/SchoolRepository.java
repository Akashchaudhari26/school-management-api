package com.sms.modules.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sms.modules.school.domain.School;

@Repository
public interface SchoolRepository extends JpaRepository<School, String> {
    boolean existsBy();
}
