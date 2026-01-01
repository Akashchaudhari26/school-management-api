package com.sms.modules.student.domain;

import lombok.Data;

@Data
public class Address {
    private String line1;
    private String line2;
    private String city;
    private String state;
    private String pincode;
    private String country;
    // getters/setters
}
