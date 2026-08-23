package com.sms.modules.school.dto.common;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ContactDto(
        @Email String email,

        @NotBlank String phone,

        String alternatePhone,

        String whatsapp) {

}
