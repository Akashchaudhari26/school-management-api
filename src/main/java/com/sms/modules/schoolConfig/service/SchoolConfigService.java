package com.sms.modules.schoolConfig.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.sms.modules.schoolConfig.domain.AcademicYear;
import com.sms.modules.schoolConfig.domain.SchoolClass;
import com.sms.modules.schoolConfig.domain.Section;
import com.sms.modules.schoolConfig.domain.Subject;
import com.sms.modules.schoolConfig.repository.AcademicYearRepository;
import com.sms.modules.schoolConfig.repository.SchoolClassRepository;
import com.sms.modules.schoolConfig.repository.SubjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SchoolConfigService {

    private final SchoolClassRepository classRepository;
    private final AcademicYearRepository yearRepository;
    private final SubjectRepository subjectRepository;

    // --- Class Management ---
    @CacheEvict(value = "classes_list", allEntries = true)
    public SchoolClass createClass(SchoolClass schoolClass) {
        return classRepository.save(schoolClass);
    }

    @Cacheable(value = "classes_list")
    public List<SchoolClass> getAllClasses() {
        // Sort by 'order' so they appear correctly in UI (Nursery -> LKG -> UKG)
        // You might need to add a custom sort here or use a Sort parameter
        return classRepository.findAll();
    }

    @CacheEvict(value = "classes_list", allEntries = true)
    public SchoolClass addSectionToClass(String classId, Section section) {
        SchoolClass schoolClass = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        // Initialize list if null
        if (schoolClass.getSections() == null) {
            schoolClass.setSections(new java.util.ArrayList<>());
        }

        schoolClass.getSections().add(section);
        return classRepository.save(schoolClass);
    }

    @CacheEvict(value = "subjects_list", allEntries = true)
    public Subject createSubject(Subject subject) {
        if (subjectRepository.existsById(subject.getId())) {
            throw new RuntimeException("Subject already exists with ID: " + subject.getId());
        }
        return subjectRepository.save(subject);
    }

    @Cacheable(value = "subjects_list")
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @CacheEvict(value = "classes_list", allEntries = true)
    public SchoolClass assignSubjectToClass(String classId, String subjectName) {
        SchoolClass schoolClass = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        // Verify subject exists first
        if (!subjectRepository.existsById(subjectName)) {
            throw new RuntimeException("Subject ID not found: " + subjectName);
        }

        if (schoolClass.getSubjectNames() == null) {
            schoolClass.setSubjectNames(new java.util.ArrayList<>());
        }

        // Avoid duplicates
        if (!schoolClass.getSubjectNames().contains(subjectName)) {
            schoolClass.getSubjectNames().add(subjectName);
        }

        return classRepository.save(schoolClass);
    }

    @CacheEvict(value = "classes_list", allEntries = true)
    public SchoolClass updateClass(String id, SchoolClass updatedClass) {
        if (!classRepository.existsById(id)) {
            throw new RuntimeException("Class not found");
        }
        // Ensure the ID in the body matches the URL
        updatedClass.setId(id);
        return classRepository.save(updatedClass);
    }

    // --- Academic Year Management ---
    @CacheEvict(value = "active_year", allEntries = true)
    public AcademicYear createAcademicYear(AcademicYear year) {
        if (year.isActive()) {
            // Deactivate others if this one is active
            AcademicYear current = yearRepository.findByIsActiveTrue().orElse(null);
            if (current != null) {
                current.setActive(false);
                yearRepository.save(current);
            }
        }
        return yearRepository.save(year);
    }

    // @Cacheable(value = "active_year")
    public AcademicYear getActiveAcademicYear() {
        return yearRepository.findByIsActiveTrue()
                .orElseThrow(() -> new RuntimeException("No active academic year configured"));
    }

    @Cacheable(value = "active_year")
    public List<AcademicYear> getAllAcademicYears() {
        return yearRepository.findAll();
    }
}