package com.sms.modules.schoolConfig.domain;

import java.time.LocalDate;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "academic_years")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcademicYear {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name; // e.g., "2025-2026"
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isActive; // Only one year should be true at a time
}