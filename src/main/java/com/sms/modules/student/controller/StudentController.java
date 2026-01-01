package com.sms.modules.student.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.sms.modules.iam.domain.User;
import com.sms.modules.student.dto.StudentCreateRequest;
import com.sms.modules.student.dto.StudentResponse;
import com.sms.modules.student.dto.StudentSearchFilter;
import com.sms.modules.student.service.StudentService;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService svc) {
	this.studentService = svc;
    }

    @PreAuthorize("hasAuthority('STUDENT_CREATE')")
    @PostMapping
    public ResponseEntity<StudentResponse> create(@RequestBody @Validated StudentCreateRequest req,
	    @AuthenticationPrincipal User user) {
	StudentResponse resp = studentService.createStudent(req, user.getId());
	return ResponseEntity.ok(resp);
    }

    @PreAuthorize("hasAuthority('STUDENT_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> get(@PathVariable String id) {
	return ResponseEntity.ok(studentService.getStudent(id));
    }

    @PreAuthorize("hasAuthority('STUDENT_READ')")
    @PostMapping("/search")
    public ResponseEntity<Page<StudentResponse>> search(@RequestBody StudentSearchFilter filter) {
	Pageable pageable = PageRequest.of(
	            filter.getPage(),
	            filter.getSize(),
	            Sort.by(filter.getDirection(), filter.getSortBy())
	    );

	return ResponseEntity.ok(studentService.searchStudents(filter, pageable));
    }

    @PreAuthorize("hasAuthority('STUDENT_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> update(@PathVariable String id, @RequestBody StudentCreateRequest req,
	    @AuthenticationPrincipal User user) {
	return ResponseEntity.ok(studentService.updateStudent(id, req, user.getId()));
    }

    @PreAuthorize("hasAuthority('STUDENT_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
	studentService.deleteStudent(id);
	return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('STUDENT_UPDATE')")
    @PostMapping("/{id}/promote")
    public ResponseEntity<StudentResponse> promote(@PathVariable String id, @RequestParam String newClassId,
	    @RequestParam(required = false) String newSection, @AuthenticationPrincipal User user) {
	return ResponseEntity.ok(studentService.promoteStudent(id, newClassId, newSection, user.getId()));
    }

    @PreAuthorize("hasAuthority('STUDENT_READ_SELF_CHILD')")
    @GetMapping("/me/children")
    public ResponseEntity<List<StudentResponse>> myChildren(@AuthenticationPrincipal User user) {
	return ResponseEntity.ok(studentService.getMyChildren(user.getId()));
    }
}
