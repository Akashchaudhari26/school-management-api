package com.sms.modules.student.domain;

import lombok.Data;

@Data
public class HealthRecord {
    private String bloodGroup;
    private String allergies;
    private String chronicDiseases;
    private String notes;
    // getters/setters
}
