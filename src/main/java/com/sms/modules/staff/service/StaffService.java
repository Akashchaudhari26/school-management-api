package com.sms.modules.staff.service;

import com.sms.modules.staff.dto.StaffCreateRequest;
import com.sms.modules.staff.dto.StaffResponse;
import com.sms.modules.staff.dto.StaffUpdateDTO;

import org.springframework.data.domain.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface StaffService {
    StaffResponse create(StaffCreateRequest dto);
    StaffResponse update(String id, StaffUpdateDTO dto);
    void softDelete(String id);
    void restore(String id);
    StaffResponse getById(String id);
    Page<StaffResponse> search(String department, String designation, String search, Pageable pageable);
    void assignIam(String id, Map<String, String> body);
    Map<String,Object> bulkImport(java.io.InputStream csvInput);
    String uploadDocument(String id, MultipartFile file);
}
