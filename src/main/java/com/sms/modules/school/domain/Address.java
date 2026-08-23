package com.sms.modules.school.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private String line1;

    private String line2;

    private String village;

    private String city;

    private String district;

    private String state;

    private String country;

    private String pinCode;

}