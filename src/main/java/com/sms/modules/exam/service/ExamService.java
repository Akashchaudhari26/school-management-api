package com.sms.modules.exam.service;

import java.util.List;

import com.sms.modules.exam.domain.ExamDefinition;
import com.sms.modules.exam.domain.ExamSubjectSchedule;
import com.sms.modules.exam.dto.BulkMarksRequest;
import com.sms.modules.exam.dto.ExamFilter;
import com.sms.modules.exam.dto.StudentReportCardResponse;

public interface ExamService {

    void saveBulkMarks(BulkMarksRequest request);

    BulkMarksRequest getMarkSheet(ExamFilter filter);

    StudentReportCardResponse generateReportCard(ExamFilter filter);

    List<StudentReportCardResponse> generateClassReportCards(ExamFilter filter);

    ExamDefinition createExamDefinition(ExamDefinition examDef);

    List<ExamDefinition> getExamsByYear(String academicYear);

    void updateClassSchedule(String examId, String className, List<ExamSubjectSchedule> schedule);

}
