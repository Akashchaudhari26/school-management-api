package com.sms.modules.exam.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sms.modules.exam.domain.ExamDefinition;
import com.sms.modules.exam.dto.BulkMarksRequest;
import com.sms.modules.exam.dto.ExamFilter;
import com.sms.modules.exam.dto.StudentReportCardResponse;
import com.sms.modules.exam.service.ExamService;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    @PostMapping("/marks/bulk")
    public ResponseEntity<?> saveMarks(@RequestBody BulkMarksRequest request) {
        examService.saveBulkMarks(request);
        return ResponseEntity.ok(Collections.singletonMap("message", "Marks saved successfully"));
    }


    @PostMapping("/marks/sheet/fetch")
    public ResponseEntity<BulkMarksRequest> getMarkSheet(
            @RequestBody ExamFilter filter) { // <--- Now uses RequestBody comfortably

        return ResponseEntity.ok(examService.getMarkSheet(filter));
    }

    @PostMapping("/report-card/fetch")
    public ResponseEntity<StudentReportCardResponse> getReportCard(
            @RequestBody ExamFilter filter) {

        return ResponseEntity.ok(examService.generateReportCard(filter));
    }

    @PostMapping("/report-card/class/fetch")
    public ResponseEntity<List<StudentReportCardResponse>> getClassReportCards(
            @RequestBody ExamFilter filter) {

        return ResponseEntity.ok(examService.generateClassReportCards(filter));
    }

    @PostMapping("/definition/create")
    public ResponseEntity<?> createExamDefinition(@RequestBody ExamDefinition examDefinition) {
        ExamDefinition savedExam = examService.createExamDefinition(examDefinition);
        return ResponseEntity.ok(Collections.singletonMap("message", "Exam Configuration Created: " + savedExam.getName()));
    }

    @GetMapping("/definition/list")
    public ResponseEntity<List<ExamDefinition>> getExamsByYear(@RequestParam String academicYear) {
        return ResponseEntity.ok(examService.getExamsByYear(academicYear));
    }
}