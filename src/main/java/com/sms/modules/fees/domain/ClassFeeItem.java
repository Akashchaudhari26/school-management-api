package com.sms.modules.fees.domain;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** JSON value stored in the class fee master; separate from the FeeItem embeddable. */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassFeeItem {
    private String name;
    private BigDecimal amount;
}
