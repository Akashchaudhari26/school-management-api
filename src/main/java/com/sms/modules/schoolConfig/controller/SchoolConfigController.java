package com.sms.modules.schoolConfig.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import com.sms.modules.schoolConfig.domain.*;
import com.sms.modules.schoolConfig.service.SchoolConfigService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/config")
@RequiredArgsConstructor
public class SchoolConfigController {

    private final SchoolConfigService configService;

    // --- ACADEMIC YEAR ---

    @PostMapping("/academic-year")
    public AcademicYear createYear(@RequestBody AcademicYear year) {
        return configService.createAcademicYear(year);
    }

    @GetMapping("/academic-year/all")
    public List<AcademicYear> getAllAcademicYears() {
        return configService.getAllAcademicYears();
    }

    @GetMapping("/academic-year/active")
    public AcademicYear getActiveYear() {
        return configService.getActiveAcademicYear();
    }

    // --- CLASSES ---

    @PostMapping("/classes")
    public SchoolClass createClass(@RequestBody SchoolClass schoolClass) {
        return configService.createClass(schoolClass);
    }

    @GetMapping("/classes")
    public List<SchoolClass> getAllClasses() {
        return configService.getAllClasses();
    }

    @PutMapping("/classes/{classId}")
    public SchoolClass updateClass(@PathVariable String classId, @RequestBody SchoolClass schoolClass) {
        return configService.updateClass(classId, schoolClass);
    }

    // --- SECTIONS ---

    @PostMapping("/classes/{classId}/sections")
    public SchoolClass addSection(@PathVariable String classId, @RequestBody Section section) {
        return configService.addSectionToClass(classId, section);
    }

    // --- SUBJECTS ---

    // 1. Create a Master Subject (e.g., Math, English)
    @PostMapping("/subjects")
    public Subject createSubject(@RequestBody Subject subject) {
        return configService.createSubject(subject);
    }

    // 2. Assign Subject to a Class
    // Example: POST /api/config/classes/NURSERY/subjects/MATH_01
    @PostMapping("/classes/{classId}/subjects/{subjectId}")
    public SchoolClass assignSubject(@PathVariable String classId, @PathVariable String subjectId) {
        return configService.assignSubjectToClass(classId, subjectId);
    }

    @GetMapping("/subjects")
    public List<Subject> getAllSubjects() {
        return configService.getAllSubjects();
    }
}