package com.sms.modules.exam.domain;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.Table;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "exam_definitions")
public class ExamDefinition {

    @Id

    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name; // e.g., "Unit Test 1"
    private String academicYear; // e.g., "2025-2026"
    private String term; // Optional: "Term 1", "Term 2"
    private boolean isActive;
    private boolean isPublished; // Have results been released to parents?

    private Date startDate; // Overall Exam Start Date
    private Date endDate; // Overall Exam End Date

    // Keep the exam-specific schedule as structured JSONB.
    @JdbcTypeCode(SqlTypes.JSON)
    private List<ClassExamConfig> classConfigs;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

}
