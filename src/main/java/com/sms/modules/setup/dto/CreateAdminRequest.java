package com.sms.modules.setup.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateAdminRequest(

                @NotBlank String fullName,

                @Email @NotBlank String email,

                @NotBlank String mobile,

                @NotBlank String password

) {
}