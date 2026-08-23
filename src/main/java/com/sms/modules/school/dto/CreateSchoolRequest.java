package com.sms.modules.school.dto;

import com.sms.modules.school.dto.common.AddressDto;
import com.sms.modules.school.dto.common.BrandingDto;
import com.sms.modules.school.dto.common.ContactDto;
import com.sms.modules.school.dto.common.ManagementDto;
import com.sms.modules.school.dto.common.RegistrationDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateSchoolRequest(

                @NotBlank String schoolName,

                String shortName,

                String tagline,

                String description,

                @Valid BrandingDto branding,

                @Valid @NotNull ContactDto contact,

                @Valid @NotNull AddressDto address,

                @Valid ManagementDto management,

                @Valid RegistrationDto registration

) {
}