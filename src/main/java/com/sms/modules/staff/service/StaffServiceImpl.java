package com.sms.modules.staff.service;

import com.sms.modules.iam.dto.RegisterUserRequest;
import com.sms.modules.iam.domain.RoleName;
import com.sms.modules.iam.repository.RoleRepository;
import com.sms.modules.iam.service.AuthService;
import com.sms.modules.staff.domain.Staff;
import com.sms.modules.staff.dto.StaffCreateRequest;
import com.sms.modules.staff.dto.StaffResponse;
import com.sms.modules.staff.dto.StaffSearchFilter;
import com.sms.modules.staff.mapper.StaffMapper;
import com.sms.modules.staff.repository.StaffRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StaffServiceImpl {

	@Autowired
	private StaffRepository repo;

	@Autowired
	private AuthService authService;

	@Autowired
	private RoleRepository roleRepository;

	public StaffResponse createStaff(StaffCreateRequest req, String userId) {
		Staff staff = StaffMapper.toEntity(req);
		String newCode = generateEmployeeCode(staff.getStaffType());
		staff.setEmployeeCode(newCode);

		Staff savedStaff = repo.save(staff);
		if (savedStaff != null) {
			RoleName roleName = parseRoleName(savedStaff.getDesignation());
			if (roleName != null && roleRepository.findByName(roleName).isPresent()) {
				RegisterUserRequest registerRequest = RegisterUserRequest.mapStaffToUserRequest(savedStaff);
				authService.register(registerRequest);
			}
		}
		return StaffMapper.toDto(staff);
	}

	public StaffResponse getById(String id) {
		Staff s = repo.findById(id).orElseThrow(() -> new RuntimeException("Staff not found"));
		return StaffMapper.toDto(s);
	}

	public List<StaffResponse> getAll(String type) {
		if (type == null) {
			return repo.findAll().stream().map(StaffMapper::toDto).toList();
		}
		return repo.findByStaffType(type).stream().map(StaffMapper::toDto).toList();
	}

	public StaffResponse update(String id, StaffCreateRequest req) {
		Staff s = repo.findById(id).orElseThrow();

		s.setFullName(req.getFullName());
		s.setDesignation(req.getDesignation());
		s.setMobile(req.getMobile());
		s.setSubjects(req.getSubjects());
		s.setAssignedClassIds(req.getAssignedClassIds());

		repo.save(s);
		return StaffMapper.toDto(s);
	}

	public void delete(String id) {
		Staff s = repo.findById(id).orElseThrow();
		repo.delete(s);
	}

	public Page<StaffResponse> searchStaff(StaffSearchFilter filter, Pageable pageable) {
		Specification<Staff> specification = (root, query, criteriaBuilder) -> {
			List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
			if (filter.getKeyword() != null && !filter.getKeyword().isBlank()) {
				String keyword = "%" + filter.getKeyword().trim().toLowerCase() + "%";
				predicates.add(criteriaBuilder.or(
						criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), keyword),
						criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), keyword),
						criteriaBuilder.like(criteriaBuilder.lower(root.get("mobile")), keyword),
						criteriaBuilder.like(criteriaBuilder.lower(root.get("adhaar")), keyword),
						criteriaBuilder.like(criteriaBuilder.lower(root.get("employeeCode")), keyword)));
			}
			if (filter.getStaffType() != null) predicates.add(criteriaBuilder.equal(root.get("staffType"), filter.getStaffType()));
			if (filter.getDesignation() != null) predicates.add(criteriaBuilder.equal(root.get("designation"), filter.getDesignation()));
			if (filter.getGender() != null) predicates.add(criteriaBuilder.equal(root.get("gender"), filter.getGender()));
			return criteriaBuilder.and(predicates.toArray(jakarta.persistence.criteria.Predicate[]::new));
		};
		return repo.findAll(specification, pageable).map(StaffMapper::toDto);
	}

	private synchronized String generateEmployeeCode(String staffType) {

		// 1. Determine Prefix based on Type
		String prefix = switch (staffType) {
			case "TEACHING" -> "TCH-";
			case "ADMIN" -> "ADM-";
			case "ACCOUNTANT" -> "ACC-";
			case "LIBRARIAN" -> "LIB-";
			case "PRINCIPAL" -> "PRI-";
			default -> "NTS-"; // Non-Teaching, Peon, Driver, etc.
		};

		// 2. Find the last staff member WITH THIS SPECIFIC PREFIX
		Staff lastStaff = repo.findTopByEmployeeCodeStartingWithOrderByEmployeeCodeDesc(prefix);

		// Case 1: No staff of this type exists yet. Start with Prefix + 001
		if (lastStaff == null || lastStaff.getEmployeeCode() == null) {
			return prefix + "001";
		}

		try {
			// Case 2: Increment the last ID
			String lastId = lastStaff.getEmployeeCode();

			// Remove the prefix to get the number (e.g., "TCH015" -> "015")
			String numericPart = lastId.replace(prefix, "");

			if (numericPart.isEmpty())
				return prefix + "001";

			int nextId = Integer.parseInt(numericPart) + 1;

			// Format: Prefix + 3 digits (e.g., TCH016)
			return String.format("%s%03d", prefix, nextId);

		} catch (Exception e) {
			// Fallback for safety
			return prefix + System.currentTimeMillis();
		}
	}

	private RoleName parseRoleName(String designation) {
		if (designation == null || designation.isBlank()) return null;
		try {
			return RoleName.valueOf(designation.trim().toUpperCase().replace(" ", "_"));
		} catch (IllegalArgumentException exception) {
			return null;
		}
	}
}
