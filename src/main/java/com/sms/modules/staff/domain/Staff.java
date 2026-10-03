package com.sms.modules.staff.domain;

import lombok.*;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "staff")
public class Staff {

    @Id

    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String fullName;
    private String email;
    private String mobile;
    private String gender;
    private LocalDate dateOfBirth;
    @Column(unique = true)
    private String adhaar;

    private String staffType; // TEACHER | NON_TEACHING
    private String designation;
    private LocalDate joiningDate;
    @Column(unique = true)
    private String employeeCode;

    // Teacher-specific fields
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> subjects;
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> assignedClassIds;

    private String tenantId;

    @Enumerated(EnumType.STRING)
    private StaffStatus status;
}
