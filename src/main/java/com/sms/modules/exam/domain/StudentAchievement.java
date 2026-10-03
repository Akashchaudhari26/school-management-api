package com.sms.modules.exam.domain;

import lombok.Data;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "student_achievements")
public class StudentAchievement {

    @Id

    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String studentId;
    private String academicYear;

    private String title; // e.g., "Inter-School Chess Champion"
    private String category; // e.g., "SPORTS", "ARTS", "ACADEMIC"
    private String description; // Details
    private LocalDate date; // When it happened

    private String certificateUrl; // Optional: Link to uploaded file
}