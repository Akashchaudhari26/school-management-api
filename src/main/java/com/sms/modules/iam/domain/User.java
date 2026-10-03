package com.sms.modules.iam.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @NotBlank(message = "Aadhaar number is required")
    // Regex: Exactly 12 digits.
    // Option: Use "^[2-9]\\d{11}$" if you want to strictly block numbers starting
    // with 0 or 1.
    @Pattern(regexp = "^\\d{12}$", message = "Aadhaar number must be exactly 12 digits")
    @Column(unique = true)
    private String adharNumber;
    @Column(unique = true)
    private String userId;

    private String fullName;
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Column(unique = true)
    private String email;

    private String password;
    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Mobile number must be valid 10 digits")
    @Column(unique = true)
    private String mobile;

    private String profileImageUrl;

    private String status; // ACTIVE / INACTIVE / SUSPENDED

    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    @JdbcTypeCode(SqlTypes.JSON)

    private List<String> permissions; // extra perms

    private String classId; // if student

    private String departmentId; // if teacher

    private String tenantId; // future multi-school

    private Instant createdAt;
    private Instant updatedAt;
    private Instant passwordChangedAt;

}
