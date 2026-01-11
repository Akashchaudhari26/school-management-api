package com.sms.modules.fees.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.util.List;

@Data
@Document(collection = "class_fee_masters")
public class ClassFeeMaster {

    @Id
    private String id;

    private String classId; // e.g., "NURSERY"
    private String academicYear; // e.g., "2025-2026"

    private List<FeeItem> feeItems; // The fixed breakdown
    private BigDecimal totalAmount; // Pre-calculated total
}