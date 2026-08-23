package com.sms.modules.school.dto;

import com.sms.modules.school.dto.common.AddressDto;
import com.sms.modules.school.dto.common.BrandingDto;
import com.sms.modules.school.dto.common.ContactDto;
import com.sms.modules.school.dto.common.ManagementDto;
import com.sms.modules.school.dto.common.RegistrationDto;

public record UpdateSchoolRequest(

                String schoolName,

                String shortName,

                String tagline,

                String description,

                BrandingDto branding,

                ContactDto contact,

                AddressDto address,

                ManagementDto management,

                RegistrationDto registration

) {
}