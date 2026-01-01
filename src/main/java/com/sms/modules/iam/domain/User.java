package com.sms.modules.iam.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Document(collection = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank(message = "Aadhaar number is required")
    // Regex: Exactly 12 digits. 
    // Option: Use "^[2-9]\\d{11}$" if you want to strictly block numbers starting with 0 or 1.
    @Pattern(regexp = "^\\d{12}$", message = "Aadhaar number must be exactly 12 digits")
    private String adharNumber;
    
    private String fullName;

    @Indexed(unique = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    private String password;

    @Indexed(unique = true)
    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Mobile number must be valid 10 digits")
    private String mobile;

    private String profileImageUrl;

    private String status; // ACTIVE / INACTIVE / SUSPENDED

    private String roleName;

    private List<String> permissions; // extra perms

    private String classId; // if student
    
    private String departmentId; // if teacher

    private String tenantId; // future multi-school

    private Instant createdAt;
    private Instant updatedAt;

}
