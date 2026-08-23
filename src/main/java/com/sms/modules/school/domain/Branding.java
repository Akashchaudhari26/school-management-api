package com.sms.modules.school.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Branding {

    private String logo;

    private String primaryColor;

    private String secondaryColor;

    private String website;

}