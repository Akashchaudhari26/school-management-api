package com.sms.modules.exam.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamSubjectSchedule {
    private String subjectName; // e.g., "Mathematics"
    private Date examDate; // e.g., 2026-10-12
    private String startTime; // e.g., "10:00 AM"
    private String duration; // e.g., "2 Hours"
    private String syllabus; // e.g., "Chapters 1, 2, and Algebra"
    private Integer subjectMaxMarks;
}