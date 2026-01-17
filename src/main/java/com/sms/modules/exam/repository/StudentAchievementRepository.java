package com.sms.modules.exam.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sms.modules.exam.domain.StudentAchievement;

import java.util.List;

public interface StudentAchievementRepository extends MongoRepository<StudentAchievement, String> {
    List<StudentAchievement> findByStudentId(String studentId);
}