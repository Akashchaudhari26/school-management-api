package com.sms.modules.staff.service;

import com.sms.modules.iam.dto.RegisterUserRequest;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StaffServiceImpl {

	@Autowired
	private StaffRepository repo;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private AuthService authService;

	@Autowired
	private RoleRepository roleRepository;

	public StaffResponse createStaff(StaffCreateRequest req, String userId) {
		Staff staff = StaffMapper.toEntity(req);
		String newCode = generateEmployeeCode(staff.getStaffType());
		staff.setEmployeeCode(newCode);

		Staff savedStaff = repo.save(staff);
		if (savedStaff != null && roleRepository.findByName(savedStaff.getDesignation()).isPresent()) {
			RegisterUserRequest registerRequest = RegisterUserRequest.mapStaffToUserRequest(savedStaff);
			authService.register(registerRequest);
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
		List<Criteria> criteriaList = new ArrayList<>();

		// 🔍 Keyword search (name, admission number)
		if (filter.getKeyword() != null && !filter.getKeyword().isBlank()) {
			String keyword = filter.getKeyword().trim();
			criteriaList.add(new Criteria().orOperator(Criteria.where("fullName").regex(keyword, "i"),
					Criteria.where("email").regex(keyword, "i"), Criteria.where("mobile").regex(keyword, "i"),
					Criteria.where("adhaar").regex(keyword, "i"), Criteria.where("employeeCode").regex(keyword, "i")));
		}

		if (filter.getStaffType() != null) {
			criteriaList.add(Criteria.where("staffType").is(filter.getStaffType()));
		}

		if (filter.getDesignation() != null) {
			criteriaList.add(Criteria.where("designation").is(filter.getDesignation()));
		}

		if (filter.getGender() != null) {
			criteriaList.add(Criteria.where("gender").is(filter.getGender()));
		}

		Criteria criteria = new Criteria();
		if (!criteriaList.isEmpty()) {
			criteria.andOperator(criteriaList.toArray(new Criteria[0]));
		}

		Query query = new Query(criteria).with(pageable);

		// 📄 Fetch data
		List<Staff> staff = mongoTemplate.find(query, Staff.class);

		// 📊 Count query (IMPORTANT)
		long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Staff.class);

		List<StaffResponse> responses = staff.stream().map(StaffMapper::toDto).toList();

		return new PageImpl<>(responses, pageable, total);
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
}
