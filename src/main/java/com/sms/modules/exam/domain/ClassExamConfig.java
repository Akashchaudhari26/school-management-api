package com.sms.modules.exam.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClassExamConfig {
    private String className; // e.g., "Class 1" or "10-A" depending on your logic
    private String classId; // Good for querying
    private List<ExamSubjectSchedule> subjects;
}
