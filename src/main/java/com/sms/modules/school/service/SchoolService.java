package com.sms.modules.school.service;

import com.sms.modules.school.dto.CreateSchoolRequest;
import com.sms.modules.school.dto.SchoolResponse;
import com.sms.modules.school.dto.UpdateSchoolRequest;

public interface SchoolService {

    SchoolResponse createSchool(CreateSchoolRequest request);

    SchoolResponse getSchoolById(String schoolId);

    SchoolResponse updateSchool(String schoolId, UpdateSchoolRequest request);

    void deleteSchool(String schoolId);
}