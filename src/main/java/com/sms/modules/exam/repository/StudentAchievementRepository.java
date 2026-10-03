package com.sms.modules.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sms.modules.exam.domain.StudentAchievement;

import java.util.List;

public interface StudentAchievementRepository extends JpaRepository<StudentAchievement, String> {
    List<StudentAchievement> findByStudentId(String studentId);
}