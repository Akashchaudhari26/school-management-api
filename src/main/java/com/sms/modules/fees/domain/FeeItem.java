package com.sms.modules.fees.domain;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeeItem {
private String name; // Tuition, Transport, Exam
private BigDecimal amount;
}