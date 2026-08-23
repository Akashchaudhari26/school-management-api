package com.sms.modules.school.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Registration {

    private String registrationNumber;

    private String udiseCode;

    private String affiliationNumber;

    private String gstNumber;

    private String panNumber;

}