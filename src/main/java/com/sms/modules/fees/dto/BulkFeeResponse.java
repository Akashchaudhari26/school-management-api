package com.sms.modules.fees.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BulkFeeResponse {
    private int totalStudentsFound;
    private int successfullyCreated;
    private int skippedAlreadyExists;
    private String message;
}