package com.sms.modules.student.service;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.sms.modules.fees.service.FeeService;
import com.sms.modules.iam.domain.User;
import com.sms.modules.iam.dto.RegisterUserRequest;
import com.sms.modules.iam.repository.UserRepository;
import com.sms.modules.iam.service.AuthService;
import com.sms.modules.student.domain.GuardianRef;
import com.sms.modules.student.domain.Student;
import com.sms.modules.student.dto.StudentCreateRequest;
import com.sms.modules.student.dto.StudentResponse;
import com.sms.modules.student.dto.StudentSearchFilter;
import com.sms.modules.student.mapper.StudentMapper;
import com.sms.modules.student.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private AuthService authService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	FeeService feeService;

	@Override
	public StudentResponse createStudent(StudentCreateRequest request, String createdBy) {
		Student s = StudentMapper.toEntity(request);
		if (s.getAdmissionNumber() == null || s.getAdmissionNumber().isBlank()) {
			String adm = generateAdmissionNumber(request.getAdmissionYear());
			s.setAdmissionNumber(adm);
		} else {
			if (studentRepository.existsByAdmissionNumber(s.getAdmissionNumber())) {
				throw new IllegalArgumentException("Admission number already exists.");
			}
		}
		s.setCreatedBy(createdBy);
		s.setUpdatedBy(createdBy);
		Student saved = studentRepository.save(s);
		try {
			feeService.assignDefaultFeeStructure(saved); // You can pass this dynamic
		} catch (Exception e) {
			// Log error but don't stop admission
			System.err.println("Failed to auto-assign fees: " + e.getMessage());
		}
		if (saved.getGuardians() != null) {
			for (GuardianRef guardian : saved.getGuardians()) {
				if (guardian.getAdharNumber() != null && !guardian.getAdharNumber().isBlank()) {
					createOrLinkParentUser(guardian);
				}
			}
		}
		return StudentMapper.toDto(saved);
	}

	private String generateAdmissionNumber(Integer year) {
		// simple generator: YEAR-UUID short
		String uuid = UUID.randomUUID().toString().split("-")[0].toUpperCase();
		return (year != null ? year : java.time.Year.now().getValue()) + "-" + uuid;
	}

	@Override
	@Cacheable(value = "students", key = "#id")
	public StudentResponse getStudent(String id) {
		Student s = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
		return StudentMapper.toDto(s);
	}

	@Override
	public Page<StudentResponse> searchStudents(StudentSearchFilter filter, Pageable pageable) {
		// Simple example: search by name or admission number using repository custom
		// methods
		// For now use findAll pageable and filter in memory for demonstration (replace
		// with proper text index)
		List<Criteria> criteriaList = new ArrayList<>();

		// 🔍 Keyword search (name, admission number)
		if (filter.getKeyword() != null && !filter.getKeyword().isBlank()) {

			String keyword = filter.getKeyword().trim();
			List<Criteria> orCriteria = new ArrayList<>();

			// Text search
			orCriteria.add(Criteria.where("firstName").regex(keyword, "i"));
			orCriteria.add(Criteria.where("lastName").regex(keyword, "i"));
			orCriteria.add(Criteria.where("admissionNumber").regex(keyword, "i"));

			// ID search (only if valid ObjectId)
			if (ObjectId.isValid(keyword)) {
				orCriteria.add(Criteria.where("_id").is(new ObjectId(keyword)));
			}

			criteriaList.add(new Criteria().orOperator(orCriteria.toArray(new Criteria[0])));
		}

		if (filter.getClassId() != null) {
			criteriaList.add(Criteria.where("currentClassId").is(filter.getClassId()));
		}

		if (filter.getSection() != null) {
			criteriaList.add(Criteria.where("currentSection").is(filter.getSection()));
		}

		if (filter.getStatus() != null) {
			criteriaList.add(Criteria.where("status").is(filter.getStatus()));
		}

		if (filter.getAdmissionYear() != null) {
			criteriaList.add(Criteria.where("admissionYear").is(filter.getAdmissionYear()));
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
		List<Student> students = mongoTemplate.find(query, Student.class);

		// 📊 Count query (IMPORTANT)
		long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Student.class);

		List<StudentResponse> responses = students.stream().map(StudentMapper::toDto).toList();

		return new PageImpl<>(responses, pageable, total);
	}

	@Override
	@CachePut(value = "students", key = "#id")
	public StudentResponse updateStudent(String id, StudentCreateRequest request, String updatedBy) {
		Student s = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
		// update fields
		if (request.getFirstName() != null)
			s.setFirstName(request.getFirstName());

		if (request.getMiddleName() != null)
			s.setMiddleName(request.getMiddleName());

		if (request.getLastName() != null)
			s.setLastName(request.getLastName());

		if (request.getDateOfBirth() != null)
			s.setDateOfBirth(request.getDateOfBirth());

		if (request.getGender() != null)
			s.setGender(request.getGender());

		if (request.getAdharNumber() != null)
			s.setAdharNumber(request.getAdharNumber());

		if (request.getPhone() != null)
			s.setPhone(request.getPhone());

		if (request.getEmail() != null)
			s.setEmail(request.getEmail());

		if (request.getAdmissionYear() != null)
			s.setAdmissionYear(request.getAdmissionYear());

		if (request.getAdmissionNumber() != null)
			s.setAdmissionNumber(request.getAdmissionNumber());

		if (request.getGuardians() != null)
			s.setGuardians(request.getGuardians());

		if (request.getCurrentClassId() != null)
			s.setCurrentClassId(request.getCurrentClassId());

		if (request.getCurrentSection() != null)
			s.setCurrentSection(request.getCurrentSection());

		if (request.getCurrentAcademicYear() != null)
			s.setCurrentAcademicYear(request.getCurrentAcademicYear());
		s.setUpdatedBy(updatedBy);
		s.setUpdatedAt(java.time.Instant.now().toEpochMilli());
		return StudentMapper.toDto(studentRepository.save(s));
	}

	@Override
	@CacheEvict(value = "students", key = "#id")
	public void deleteStudent(String id) {
		studentRepository.deleteById(id);
	}

	@Override
	public StudentResponse admitStudent(String applicationId, String createdBy) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	@CachePut(value = "students", key = "#studentId")
	public StudentResponse promoteStudent(String studentId, String newClassId, String newSection, String promotedBy) {
		Student s = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
		s.setCurrentClassId(newClassId);
		s.setCurrentSection(newSection);
		s.setUpdatedBy(promotedBy);
		s.setUpdatedAt(java.time.Instant.now().toEpochMilli());
		return StudentMapper.toDto(studentRepository.save(s));
	}

	@Override
	public List<StudentResponse> getMyChildren(String parentUserId) {
		return studentRepository.findByGuardiansIamUserId(parentUserId).stream().map(StudentMapper::toDto).toList();
	}

	private void createOrLinkParentUser(GuardianRef guardian) {
		try {
			User user;

			// Step A: Check if User already exists (e.g., Sibling's parent)
			Optional<User> existingUser = userRepository.findByAdharNumber(guardian.getAdharNumber());

			if (existingUser.isPresent()) {
				user = existingUser.get();
			} else {
				// Step B: Create New User if not exists
				RegisterUserRequest req = RegisterUserRequest.mapGuardianToUserRequest(guardian);
				user = authService.register(req);
			}

			// Step C: Link the Student's Guardian entry to this User ID
			// (This uses your custom @Update query to set iamUserId)
			studentRepository.linkGuardianToIamUser(guardian.getAdharNumber(), user.getId());

		} catch (Exception e) {
			// Log error but don't fail student creation?
			// Or throw exception to rollback?
			// Usually better to log for parents so student data isn't lost.
			System.err.println("Failed to create user for guardian: " + guardian.getName() + " - " + e.getMessage());
			e.printStackTrace();
		}
	}
}