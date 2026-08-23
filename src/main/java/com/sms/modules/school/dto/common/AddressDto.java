package com.sms.modules.school.dto.common;

public record AddressDto(

                String line1,

                String line2,

                String village,

                String city,

                String district,

                String state,

                String country,

                String pinCode,

                Double latitude,

                Double longitude

) {
}