package com.sms.modules.fees.domain;

import lombok.Data;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@Table(name = "class_fee_masters")
public class ClassFeeMaster {

    @Id

    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String classId; // e.g., "NURSERY"
    private String academicYear; // e.g., "2025-2026"

    @JdbcTypeCode(SqlTypes.JSON)

    private List<ClassFeeItem> feeItems; // The fixed breakdown stored as JSONB
    @Column(precision = 12, scale = 2)
    private BigDecimal totalAmount; // Pre-calculated total
}
