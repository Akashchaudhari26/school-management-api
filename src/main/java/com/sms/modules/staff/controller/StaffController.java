package com.sms.modules.staff.controller;

import com.sms.modules.staff.dto.StaffCreateRequest;
import com.sms.modules.staff.dto.StaffResponse;
import com.sms.modules.staff.dto.StaffSearchFilter;
import com.sms.modules.staff.service.StaffServiceImpl;
import com.sms.security.SecurityUtils;

import org.apache.catalina.security.SecurityUtil;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

	private final StaffServiceImpl svc;

	public StaffController(StaffServiceImpl svc) {
		this.svc = svc;
	}

	@PreAuthorize("hasAuthority('STAFF_ADD')")
	@PostMapping
	public StaffResponse create(@RequestBody StaffCreateRequest req) {
		return svc.createStaff(req, SecurityUtils.getCurrentUserId());
	}

	@PreAuthorize("hasAuthority('STAFF_READ')")
	@GetMapping("/{id}")
	public StaffResponse getOne(@PathVariable String id) {
		return svc.getById(id);
	}

	@PreAuthorize("hasAuthority('STAFF_READ')")
	@GetMapping
	public List<StaffResponse> getAll(@RequestParam String type) {
		return svc.getAll(type);
	}

	@PreAuthorize("hasAuthority('STAFF_UPDATE')")
	@PutMapping("/{id}")
	public StaffResponse update(@PathVariable String id, @RequestBody StaffCreateRequest req) {
		return svc.update(id, req);
	}

	@PreAuthorize("hasAuthority('STAFF_DELETE')")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable String id) {
		svc.delete(id);
	}
	
	@PreAuthorize("hasAuthority('STAFF_READ')")
	@PostMapping("/search")
	public ResponseEntity<Page<StaffResponse>> search(@RequestBody StaffSearchFilter filter) {
	    Pageable pageable = PageRequest.of(
	            filter.getPage(),
	            filter.getSize(),
	            Sort.by(filter.getDirection(), filter.getSortBy())
	    );

	    
	    // You need to implement this method in your Service
	    return ResponseEntity.ok(svc.searchStaff(filter, pageable));
	}
}
