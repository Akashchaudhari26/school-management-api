package com.sms.modules.staff.dto;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class StaffSearchFilter {
    // 🔍 Search Filters
    private String keyword;       // name, email, mobile, empCode
    private String staffType;     // TEACHER, NON_TEACHING, ADMIN
    private String designation;   // e.g., "Principal", "Peon"
    private String gender;

    // 📄 Pagination
    private int page = 0;
    private int size = 20;

    // ↕ Sorting
    private String sortBy = "fullName";
    private Sort.Direction direction = Sort.Direction.ASC;
}