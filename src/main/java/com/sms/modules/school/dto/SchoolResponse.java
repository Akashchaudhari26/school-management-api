package com.sms.modules.school.dto;

import java.time.LocalDateTime;

import com.sms.modules.school.dto.common.AddressDto;
import com.sms.modules.school.dto.common.BrandingDto;
import com.sms.modules.school.dto.common.ContactDto;
import com.sms.modules.school.dto.common.ManagementDto;
import com.sms.modules.school.dto.common.RegistrationDto;
import com.sms.modules.school.enums.SchoolStatus;

public record SchoolResponse(

                String id,

                String schoolName,

                String shortName,

                String tagline,

                String description,

                BrandingDto branding,

                ContactDto contact,

                AddressDto address,

                ManagementDto management,

                RegistrationDto registration,

                SchoolStatus status,

                LocalDateTime createdAt,

                LocalDateTime updatedAt

) {
}