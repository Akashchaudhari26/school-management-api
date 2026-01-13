package com.sms.modules.student.mapper;

import java.time.Instant;

import com.sms.modules.student.domain.Student;
import com.sms.modules.student.domain.StudentStatus;
import com.sms.modules.student.dto.StudentCreateRequest;
import com.sms.modules.student.dto.StudentResponse;

public class StudentMapper {
    public static Student toEntity(StudentCreateRequest req) {
        Student s = new Student();
        s.setFirstName(req.getFirstName());
        s.setMiddleName(req.getMiddleName());
        s.setLastName(req.getLastName());
        s.setDateOfBirth(req.getDateOfBirth());
        s.setGender(req.getGender());
        s.setPhone(req.getPhone());
        s.setEmail(req.getEmail());
        s.setAdmissionYear(req.getAdmissionYear());
        s.setAdmissionNumber(req.getAdmissionNumber());
        s.setStatus(StudentStatus.ACTIVE);
        s.setCreatedAt(Instant.now().toEpochMilli());
        s.setUpdatedAt(Instant.now().toEpochMilli());
        s.setGuardians(req.getGuardians());
        s.setCurrentClassId(req.getCurrentClassId());
        s.setCurrentSection(req.getCurrentSection());
        s.setCurrentAcademicYear(req.getCurrentAcademicYear());
        // map guardians etc
        return s;
    }

    public static StudentResponse toDto(Student s) {
        StudentResponse r = new StudentResponse();
        r.setId(s.getId());
        r.setFirstName(s.getFirstName());
        r.setMiddleName(s.getMiddleName());
        r.setLastName(s.getLastName());
        r.setDateOfBirth(s.getDateOfBirth());
        r.setGender(s.getGender());
        r.setAdmissionNumber(s.getAdmissionNumber());
        r.setAdmissionYear(s.getAdmissionYear());
        r.setStatus(s.getStatus());
        r.setCurrentClassId(s.getCurrentClassId());
        r.setCurrentSection(s.getCurrentSection());
        r.setGuardians(s.getGuardians());
        r.setCurrentClassId(s.getCurrentClassId());
        r.setCurrentSection(s.getCurrentSection());
        r.setCurrentAcademicYear(s.getCurrentAcademicYear());
        // map guardians etc
        return r;
    }
}
