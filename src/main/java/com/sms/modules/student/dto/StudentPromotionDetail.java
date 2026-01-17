package com.sms.modules.student.dto;

import lombok.Data;

@Data
public class StudentPromotionDetail {
    private String studentId;
    private String promotionStatus; // PROMOTE, RETAIN, DEMOTE
    private String targetClassId; // Where are they going?
    private String targetSection;
}