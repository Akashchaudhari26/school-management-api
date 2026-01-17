package com.sms.modules.exam.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Document(collection = "exam_definitions")
public class ExamDefinition {

    @Id
    private String id;

    private String name; // e.g., "Unit Test 1"
    private String academicYear; // e.g., "2025-2026"
    private String term; // Optional: "Term 1", "Term 2"
    private boolean isActive;
    private boolean isPublished; // Have results been released to parents?

    private Date startDate; // Overall Exam Start Date
    private Date endDate; // Overall Exam End Date

    // Embed the rules directly since MongoDB allows nested arrays
    private List<ClassExamConfig> classConfigs;

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private LocalDateTime updatedAt;

}