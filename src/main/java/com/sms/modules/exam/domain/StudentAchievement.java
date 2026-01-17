package com.sms.modules.exam.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Data
@Document(collection = "student_achievements")
public class StudentAchievement {

    @Id
    private String id;

    private String studentId;
    private String academicYear;

    private String title; // e.g., "Inter-School Chess Champion"
    private String category; // e.g., "SPORTS", "ARTS", "ACADEMIC"
    private String description; // Details
    private LocalDate date; // When it happened

    private String certificateUrl; // Optional: Link to uploaded file
}