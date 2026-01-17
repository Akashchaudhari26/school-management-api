package com.sms.modules.exam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sms.modules.exam.domain.ExamDefinition;
import com.sms.modules.exam.domain.ExamResult;
import com.sms.modules.exam.domain.ExamSubjectSchedule;
import com.sms.modules.exam.dto.BulkMarksRequest;
import com.sms.modules.exam.dto.ExamFilter;
import com.sms.modules.exam.dto.StudentMarkEntry;
import com.sms.modules.exam.dto.StudentReportCardResponse;
import com.sms.modules.exam.dto.SubjectMark;
import com.sms.modules.exam.repository.ExamDefinitionRepository;
import com.sms.modules.exam.repository.ExamResultRepository;
import com.sms.modules.student.domain.Student;
import com.sms.modules.student.repository.StudentRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Date;

@Service
public class ExamServiceImpl implements ExamService {

    @Autowired
    private ExamResultRepository examRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ExamDefinitionRepository examDefinitionRepository;

    // --- 1. SAVE BULK MARKS ---
    @Transactional
    @Override
    public void saveBulkMarks(BulkMarksRequest request) {
        // Fetch existing marks to avoid duplicates (Update logic)
        List<ExamResult> existingResults = examRepository.findByClassIdAndAcademicYearAndExamNameAndSubjectName(
                request.getClassId(), request.getAcademicYear(), request.getExamName(), request.getSubjectName());

        // Map for quick lookup: StudentID -> ExamResult
        Map<String, ExamResult> resultMap = existingResults.stream()
                .collect(Collectors.toMap(ExamResult::getStudentId, e -> e));

        List<ExamResult> toSave = new ArrayList<>();

        for (StudentMarkEntry entry : request.getStudentMarks()) {
            ExamResult result = resultMap.getOrDefault(entry.getStudentId(), new ExamResult());

            // Set Keys (If new)
            if (result.getId() == null) {
                result.setStudentId(entry.getStudentId());
                result.setClassId(request.getClassId());
                result.setAcademicYear(request.getAcademicYear());
                result.setExamName(request.getExamName());
                result.setSubjectName(request.getSubjectName());
            }

            // Update Values
            result.setTotalMarks(request.getTotalMarks());
            result.setMarksObtained(entry.getMarksObtained());
            result.setRemarks(entry.getRemarks());

            // Auto-Calculate Grade (Simple Logic)
            result.setGrade(calculateGrade(entry.getMarksObtained(), request.getTotalMarks()));

            toSave.add(result);
        }

        examRepository.saveAll(toSave);
    }

    // --- 2. FETCH MARK SHEET (For UI) ---
    @Override
    public BulkMarksRequest getMarkSheet(ExamFilter filter) {

        // A. Get All Students in Class
        List<Student> students = studentRepository.findByCurrentClassIdAndCurrentSection(filter.getClassId(),
                filter.getSection());

        // B. Get Existing Marks (if any)
        List<ExamResult> existingMarks = examRepository.findByClassIdAndAcademicYearAndExamNameAndSubjectName(
                filter.getClassId(), filter.getAcademicYear(), filter.getExamName(), filter.getSubjectName());
        Map<String, ExamResult> marksMap = existingMarks.stream()
                .collect(Collectors.toMap(ExamResult::getStudentId, e -> e));

        // C. Merge into Response
        BulkMarksRequest response = new BulkMarksRequest();
        response.setClassId(filter.getClassId());
        response.setExamName(filter.getExamName());
        response.setSubjectName(filter.getSubjectName());
        response.setSection(filter.getSection());
        response.setAcademicYear(filter.getAcademicYear());
        response.setTotalMarks(existingMarks.stream().findFirst().map(ExamResult::getTotalMarks).orElse(0.0));
        List<StudentMarkEntry> entries = students.stream().map(student -> {
            StudentMarkEntry entry = new StudentMarkEntry();
            entry.setAdmissionNumber(student.getAdmissionNumber());
            entry.setStudentId(student.getId());
            entry.setStudentName(student.getFirstName() + " " + student.getLastName());
            // If marks exist, use them. Else null (frontend shows empty box)
            if (marksMap.containsKey(student.getId())) {
                entry.setMarksObtained(marksMap.get(student.getId()).getMarksObtained());
                entry.setRemarks(marksMap.get(student.getId()).getRemarks());
            }
            return entry;
        }).collect(Collectors.toList());

        response.setStudentMarks(entries);
        return response;
    }

    @Override
    public StudentReportCardResponse generateReportCard(ExamFilter filter) {

        // 1. Fetch Student Details
        Student student = studentRepository.findById(filter.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // 2. Fetch Marks for ALL subjects for this specific Exam
        List<ExamResult> allResults = examRepository.findByStudentIdAndAcademicYear(filter.getStudentId(),
                filter.getAcademicYear());
        List<ExamResult> examResults = allResults.stream()
                .filter(r -> r.getExamName().equalsIgnoreCase(filter.getExamName()))
                .collect(Collectors.toList());

        if (examResults.isEmpty()) {
            throw new RuntimeException("No marks found for this exam.");
        }

        // 3. Map Subjects & Calculate Totals
        List<SubjectMark> subjects = new ArrayList<>();
        double totalObtained = 0;
        double maxTotal = 0;

        for (ExamResult r : examResults) {
            subjects.add(SubjectMark.builder()
                    .subjectName(r.getSubjectName())
                    .marksObtained(r.getMarksObtained())
                    .totalMarks(r.getTotalMarks())
                    .grade(r.getGrade())
                    .remarks(r.getRemarks())
                    .build());

            totalObtained += r.getMarksObtained();
            maxTotal += r.getTotalMarks();
        }

        // 4. Calculate Final Stats
        double percentage = (maxTotal > 0) ? (totalObtained / maxTotal) * 100 : 0;
        String finalGrade = calculateGrade(totalObtained, maxTotal); // Reuse your helper method
        String status = percentage >= 33 ? "PASS" : "FAIL"; // Basic logic

        // 5. Build Response
        return StudentReportCardResponse.builder()
                .studentName(student.getFirstName() + " " + student.getLastName())
                .admissionNumber(student.getAdmissionNumber())
                .className(student.getCurrentClassId())
                .section(student.getCurrentSection())
                .academicYear(filter.getAcademicYear())
                .examName(filter.getExamName())
                .subjects(subjects)
                .totalMarksObtained(totalObtained)
                .maxTotalMarks(maxTotal)
                .percentage(Math.round(percentage * 100.0) / 100.0) // Round to 2 decimals
                .finalGrade(finalGrade)
                .resultStatus(status)
                .build();
    }

    @Override
    public List<StudentReportCardResponse> generateClassReportCards(ExamFilter filter) {

        // 1. Fetch ALL Students in the class (Query #1)
        List<Student> students = studentRepository.findByCurrentClassIdAndCurrentSection(filter.getClassId(),
                filter.getSection());

        // 2. Fetch ALL Exam Results for this class & exam (Query #2)
        List<ExamResult> allExamResults = examRepository.findByClassIdAndAcademicYearAndExamName(
                filter.getClassId(), filter.getAcademicYear(), filter.getExamName());
        // 3. Group Results by StudentID for instant lookup
        // Map<StudentId, List<ExamResult>>
        Map<String, List<ExamResult>> studentResultsMap = allExamResults.stream()
                .collect(Collectors.groupingBy(ExamResult::getStudentId));

        List<StudentReportCardResponse> reportCards = new ArrayList<>();

        // 4. Iterate Students and Build Reports
        for (Student student : students) {

            // Get marks for this specific student (or empty list if absent/not entered)
            List<ExamResult> studentMarks = studentResultsMap.getOrDefault(student.getId(), new ArrayList<>());

            // --- CALCULATE STATS ---
            List<SubjectMark> subjects = new ArrayList<>();
            double totalObtained = 0;
            double maxTotal = 0;

            for (ExamResult r : studentMarks) {
                subjects.add(SubjectMark.builder()
                        .subjectName(r.getSubjectName())
                        .marksObtained(r.getMarksObtained())
                        .totalMarks(r.getTotalMarks())
                        .grade(r.getGrade())
                        .remarks(r.getRemarks())
                        .build());

                totalObtained += r.getMarksObtained();
                maxTotal += r.getTotalMarks();
            }

            double percentage = (maxTotal > 0) ? (totalObtained / maxTotal) * 100 : 0;
            String finalGrade = calculateGrade(totalObtained, maxTotal);
            String status = percentage >= 33 ? "PASS" : "FAIL";

            // --- BUILD DTO ---
            reportCards.add(StudentReportCardResponse.builder()
                    .studentName(student.getFirstName() + " " + student.getLastName())
                    .admissionNumber(student.getAdmissionNumber())
                    .className(student.getCurrentClassId())
                    .section(student.getCurrentSection())
                    .academicYear(filter.getAcademicYear())
                    .examName(filter.getExamName())
                    .subjects(subjects)
                    .totalMarksObtained(totalObtained)
                    .maxTotalMarks(maxTotal)
                    .percentage(Math.round(percentage * 100.0) / 100.0)
                    .finalGrade(finalGrade)
                    .resultStatus(status)
                    .build());
        }

        return reportCards;
    }

    // 1. Create or Update Exam Configuration
    @Override
    public ExamDefinition createExamDefinition(ExamDefinition examDef) {
        // Basic validation
        if (examDef.getAcademicYear() == null || examDef.getName() == null) {
            throw new RuntimeException("Exam Name and Academic Year are required");
        }

        // Optional: Check for duplicates if it's a new record (id is null)
        if (examDef.getId() == null) {
            examDefinitionRepository.findByNameAndAcademicYear(examDef.getName(), examDef.getAcademicYear())
                    .ifPresent(e -> {
                        throw new RuntimeException("Exam with this name already exists for this year");
                    });
        }

        examDef.setActive(true);
        return examDefinitionRepository.save(examDef);
    }

    // 2. Fetch All Exams for a specific Year (For Dropdowns)
    @Override
    public List<ExamDefinition> getExamsByYear(String academicYear) {
        return examDefinitionRepository.findByAcademicYear(academicYear);
    }

    @Override
    public void updateClassSchedule(String examId, String className, List<ExamSubjectSchedule> schedule) {
        ExamDefinition exam = examDefinitionRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        // Find the specific class config and update its subjects
        exam.getClassConfigs().stream()
                .filter(c -> c.getClassName().equals(className))
                .findFirst()
                .ifPresent(config -> config.setSubjects(schedule));

        examDefinitionRepository.save(exam);
    }

    private String calculateGrade(Double marks, Double total) {
        if (marks == null || total == null)
            return "N/A";
        double percentage = (marks / total) * 100;
        if (percentage >= 90)
            return "A+";
        if (percentage >= 80)
            return "A";
        if (percentage >= 70)
            return "B";
        if (percentage >= 50)
            return "C";
        return "D";
    }

}