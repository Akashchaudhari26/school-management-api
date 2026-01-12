package com.sms.modules.schoolConfig.domain;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "academic_years")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcademicYear {
    @Id
    private String id;
    private String name; // e.g., "2025-2026"
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isActive; // Only one year should be true at a time
}