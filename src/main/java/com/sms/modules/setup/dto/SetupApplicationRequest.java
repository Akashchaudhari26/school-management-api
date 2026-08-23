package com.sms.modules.setup.dto;

import com.sms.modules.school.dto.CreateSchoolRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record SetupApplicationRequest(

                @Valid @NotNull CreateSchoolRequest school,

                @Valid @NotNull CreateAdminRequest admin

) {
}