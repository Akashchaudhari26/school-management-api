package com.sms.modules.student.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sms.modules.fees.service.FeeService;
import com.sms.modules.iam.domain.User;
import com.sms.modules.iam.dto.RegisterUserRequest;
import com.sms.modules.iam.repository.UserRepository;
import com.sms.modules.iam.service.AuthService;
import com.sms.modules.student.domain.GuardianRef;
import com.sms.modules.student.domain.Student;
import com.sms.modules.student.domain.StudentAcademicHistory;
import com.sms.modules.student.dto.PromotionRequest;
import com.sms.modules.student.dto.StudentCreateRequest;
import com.sms.modules.student.dto.StudentPromotionDetail;
import com.sms.modules.student.dto.StudentResponse;
import com.sms.modules.student.dto.StudentSearchFilter;
import com.sms.modules.student.mapper.StudentMapper;
import com.sms.modules.student.repository.StudentAcademicHistoryRepository;
import com.sms.modules.student.repository.StudentRepository;

import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private AuthService authService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private StudentAcademicHistoryRepository studentAcademicHistoryRepository;

	@Autowired
	FeeService feeService;

	@Override
	@Transactional
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
	@Transactional(readOnly = true)
	@Cacheable(value = "students", key = "#id")
	public StudentResponse getStudent(String id) {
		Student s = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
		return StudentMapper.toDto(s);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<StudentResponse> searchStudents(StudentSearchFilter filter, Pageable pageable) {
		Specification<Student> specification = (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (filter.getKeyword() != null && !filter.getKeyword().isBlank()) {
				String keyword = "%" + filter.getKeyword().trim().toLowerCase() + "%";
				predicates.add(criteriaBuilder.or(
						criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), keyword),
						criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), keyword),
						criteriaBuilder.like(criteriaBuilder.lower(root.get("admissionNumber")), keyword),
						criteriaBuilder.equal(root.get("id"), filter.getKeyword().trim())));
			}
			if (filter.getClassId() != null)
				predicates.add(criteriaBuilder.equal(root.get("currentClassId"), filter.getClassId()));
			if (filter.getSection() != null)
				predicates.add(criteriaBuilder.equal(root.get("currentSection"), filter.getSection()));
			if (filter.getStatus() != null)
				predicates.add(criteriaBuilder.equal(root.get("status"), filter.getStatus()));
			if (filter.getAdmissionYear() != null)
				predicates.add(criteriaBuilder.equal(root.get("admissionYear"), filter.getAdmissionYear()));
			if (filter.getGender() != null)
				predicates.add(criteriaBuilder.equal(root.get("gender"), filter.getGender()));
			return criteriaBuilder.and(predicates.toArray(jakarta.persistence.criteria.Predicate[]::new));
		};
		return studentRepository.findAll(specification, pageable).map(StudentMapper::toDto);
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
	@Transactional
	public void promoteStudents(PromotionRequest request) {
		// 1. Fetch all students in one query (Performance Optimization)
		List<String> studentIds = request.getStudents().stream()
				.map(StudentPromotionDetail::getStudentId)
				.collect(Collectors.toList());

		List<Student> students = studentRepository.findAllById(studentIds);

		// Map for quick lookup
		Map<String, Student> studentMap = students.stream()
				.collect(Collectors.toMap(Student::getId, s -> s));

		List<Student> studentsToSave = new ArrayList<>();
		List<StudentAcademicHistory> historyToSave = new ArrayList<>();

		// 2. Iterate and Process
		for (StudentPromotionDetail detail : request.getStudents()) {
			Student student = studentMap.get(detail.getStudentId());

			if (student == null)
				continue; // Skip invalid IDs

			// --- A. ARCHIVE HISTORY (The "Professional" Step) ---
			// Save their CURRENT state before we change it
			StudentAcademicHistory history = StudentAcademicHistory.builder()
					.studentId(student.getId())
					.academicYear(student.getCurrentAcademicYear()) // The year ending
					.classId(student.getCurrentClassId())
					.section(student.getCurrentSection())
					.result(detail.getPromotionStatus())
					.promotedAt(System.currentTimeMillis())
					.build();
			historyToSave.add(history);

			// --- B. UPDATE STUDENT ---
			switch (detail.getPromotionStatus()) {
				case "PROMOTE":
				case "DEMOTE":
					student.setCurrentClassId(detail.getTargetClassId());
					student.setCurrentSection(detail.getTargetSection());
					break;
				case "RETAIN":
					// Class/Section stays same, but they enter the NEW Academic Year
					break;
			}

			// Set the NEW Academic Year
			student.setCurrentAcademicYear(request.getTargetAcademicYear());
			studentsToSave.add(student);
		}

		// 3. BATCH SAVE (Performance)
		if (!historyToSave.isEmpty()) {
			studentAcademicHistoryRepository.saveAll(historyToSave);
		}

		if (!studentsToSave.isEmpty()) {
			List<Student> savedStudents = studentRepository.saveAll(studentsToSave);

			// 4. AUTOMATED FEE TRIGGER (Integration)
			// Now that students are in the NEW class, generate their fees instantly
			for (Student s : savedStudents) {
				try {
					feeService.assignDefaultFeeStructure(s);
				} catch (Exception e) {
					System.err.println("Fee generation failed for " + s.getId());
					// Don't rollback transaction for fee error, just log it
				}
			}
		}
	}

	@Override
	@Transactional(readOnly = true)
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
			List<Student> linkedStudents = studentRepository.findByGuardiansAdharNumber(guardian.getAdharNumber());
			for (Student student : linkedStudents) {
				for (GuardianRef studentGuardian : student.getGuardians()) {
					if (guardian.getAdharNumber().equals(studentGuardian.getAdharNumber())
							&& (studentGuardian.getIamUserId() == null || studentGuardian.getIamUserId().isBlank())) {
						studentGuardian.setIamUserId(user.getId());
					}
				}
			}
			studentRepository.saveAll(linkedStudents);

		} catch (Exception e) {
			// Log error but don't fail student creation?
			// Or throw exception to rollback?
			// Usually better to log for parents so student data isn't lost.
			System.err.println("Failed to create user for guardian: " + guardian.getName() + " - " + e.getMessage());
			e.printStackTrace();
		}
	}
}
