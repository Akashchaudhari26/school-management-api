package com.sms.modules.student.dto;

import lombok.Data;
import java.util.List;

@Data
public class PromotionRequest {
    private String targetAcademicYear; // e.g., "2026-2027"
    private List<StudentPromotionDetail> students;
}