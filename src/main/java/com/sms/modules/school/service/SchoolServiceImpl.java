package com.sms.modules.school.service;

import org.springframework.stereotype.Service;

import com.sms.modules.school.domain.School;
import com.sms.modules.school.dto.CreateSchoolRequest;
import com.sms.modules.school.dto.SchoolResponse;
import com.sms.modules.school.dto.UpdateSchoolRequest;
import com.sms.modules.school.mapper.SchoolMapper;
import com.sms.modules.school.repository.SchoolRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;

    private final SchoolMapper schoolMapper;

    @Override
    public SchoolResponse createSchool(CreateSchoolRequest request) {
        School school = schoolMapper.toEntity(request);
        schoolRepository.save(school);
        return schoolMapper.toResponse(school);
    }

    @Override
    public void deleteSchool(String schoolId) {
        // TODO Auto-generated method stub
        schoolRepository.deleteById(schoolId);

    }

    @Override
    public SchoolResponse getSchoolById(String schoolId) {
        // TODO Auto-generated method stub
        School school = schoolRepository.findById(schoolId).orElseThrow(() -> new RuntimeException("School not found"));
        return schoolMapper.toResponse(school);
    }

    @Override
    public SchoolResponse updateSchool(String schoolId, UpdateSchoolRequest request) {
        // TODO Auto-generated method stub
        School school = schoolRepository.findById(schoolId).orElseThrow(() -> new RuntimeException("School not found"));
        schoolMapper.updateEntity(request, school);
        schoolRepository.save(school);
        return schoolMapper.toResponse(school);
    }
}
