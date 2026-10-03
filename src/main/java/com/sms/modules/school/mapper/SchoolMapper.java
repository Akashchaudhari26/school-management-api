package com.sms.modules.school.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.sms.modules.school.domain.School;
import com.sms.modules.school.dto.CreateSchoolRequest;
import com.sms.modules.school.dto.SchoolResponse;
import com.sms.modules.school.dto.UpdateSchoolRequest;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SchoolMapper {

    School toEntity(CreateSchoolRequest request);

    SchoolResponse toResponse(School school);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(UpdateSchoolRequest request, @MappingTarget School school);

}