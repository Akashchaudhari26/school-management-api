package com.sms.modules.schoolConfig.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "subjects")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Subject {
    @Id
    private String id;
    private String name; // e.g., "Mathematics"
    private String code; // e.g., "MATH-01"
    private String classId; // Reference to SchoolClass ID
    private boolean isOptional; // For higher standards
}