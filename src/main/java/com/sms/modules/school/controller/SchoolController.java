package com.sms.modules.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sms.modules.school.dto.CreateSchoolRequest;
import com.sms.modules.school.dto.UpdateSchoolRequest;
import com.sms.modules.school.dto.SchoolResponse;
import com.sms.modules.school.service.SchoolService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/school")
@Validated
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolService schoolService;

    @PostMapping
    public ResponseEntity<SchoolResponse> createSchool(
            @Valid @RequestBody CreateSchoolRequest request) {

        SchoolResponse response = schoolService.createSchool(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{schoolId}")
    public ResponseEntity<SchoolResponse> getSchoolById(
            @PathVariable String schoolId) {

        return ResponseEntity.ok(
                schoolService.getSchoolById(schoolId));
    }

    @PutMapping("/{schoolId}")
    public ResponseEntity<SchoolResponse> updateSchool(
            @PathVariable String schoolId,
            @Valid @RequestBody UpdateSchoolRequest request) {

        return ResponseEntity.ok(
                schoolService.updateSchool(schoolId, request));
    }

    @DeleteMapping("/{schoolId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSchool(
            @PathVariable String schoolId) {

        schoolService.deleteSchool(schoolId);
    }
}