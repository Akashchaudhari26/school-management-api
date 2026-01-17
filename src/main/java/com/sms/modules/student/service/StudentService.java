package com.sms.modules.student.service;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sms.modules.student.dto.PromotionRequest;
import com.sms.modules.student.dto.StudentCreateRequest;
import com.sms.modules.student.dto.StudentResponse;
import com.sms.modules.student.dto.StudentSearchFilter;

public interface StudentService {
    StudentResponse createStudent(StudentCreateRequest request, String createdBy);
    StudentResponse getStudent(String id);
    Page<StudentResponse> searchStudents(StudentSearchFilter filter, Pageable pageable);
    StudentResponse updateStudent(String id, StudentCreateRequest request, String updatedBy);
    void deleteStudent(String id);
    StudentResponse admitStudent(String applicationId, String createdBy); // if you manage applications separate
    StudentResponse promoteStudent(String studentId, String newClassId, String newSection, String promotedBy);
    public List<StudentResponse> getMyChildren(String parentUserId);
    void promoteStudents(PromotionRequest request);
}
