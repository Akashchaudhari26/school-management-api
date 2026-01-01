package com.sms.modules.fees.dto;

import lombok.Data;
import java.util.List;

import com.sms.modules.fees.domain.FeeItem;

@Data
public class FeeCreateRequest {
    private String	  studentId;
    private String	  academicYear;
    private List<FeeItem> feeItems;
}