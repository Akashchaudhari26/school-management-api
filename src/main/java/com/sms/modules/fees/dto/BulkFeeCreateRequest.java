package com.sms.modules.fees.dto;

import java.util.List;

import com.sms.modules.fees.domain.FeeItem;

import lombok.Data;

@Data
public class BulkFeeCreateRequest {
    private String classId; // e.g., "NURSERY"
    private String section; // e.g., "A" (Optional, if null apply to all sections)
    private String academicYear; // e.g., "2025-2026"
    private List<FeeItem> feeItems; // The structure to copy
}