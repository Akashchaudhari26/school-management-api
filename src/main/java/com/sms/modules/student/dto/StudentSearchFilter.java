package com.sms.modules.student.dto;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class StudentSearchFilter {

    // 🔍 Search & filters
    private String keyword;          // name, admission no, phone
    private String classId;
    private String section;
    private String status;            // ACTIVE / INACTIVE
    private Integer admissionYear;
    private String gender;

    // 📄 Pagination
    private int page = 0;
    private int size = 20;

    // ↕ Sorting
    private String sortBy = "lastName";
    private Sort.Direction direction = Sort.Direction.ASC;
}
