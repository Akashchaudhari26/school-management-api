package com.sms.modules.school.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contact {

    private String email;

    private String phone;

    private String alternatePhone;

    private String whatsapp;

}