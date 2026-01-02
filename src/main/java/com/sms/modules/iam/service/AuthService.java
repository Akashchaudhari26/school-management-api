package com.sms.modules.iam.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sms.modules.iam.domain.Role;
import com.sms.modules.iam.domain.User;
import com.sms.modules.iam.dto.AuthResponse;
import com.sms.modules.iam.dto.LoginRequest;
import com.sms.modules.iam.dto.RegisterUserRequest;
import com.sms.modules.iam.repository.RoleRepository;
import com.sms.modules.iam.repository.UserRepository;
import com.sms.modules.student.domain.GuardianRef;
import com.sms.modules.student.domain.Student;
import com.sms.modules.student.repository.StudentRepository;
import com.sms.security.JwtTokenProvider;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	public AuthResponse login(LoginRequest request) {
		if ((request.getEmail() == null || request.getEmail().isBlank())
				&& (request.getMobile() == null || request.getMobile().isBlank())
				&& (request.getAdharNumber() == null || request.getAdharNumber().isBlank())) {
			throw new RuntimeException("Email or mobile is required");
		}

		Optional<User> ou;

		if (request.getEmail() != null && !request.getEmail().isBlank()) {
			ou = userRepository.findByEmail(request.getEmail());
		} else if (request.getMobile() != null && !request.getMobile().isBlank()) {
			ou = userRepository.findByMobile(request.getMobile());
		} else {
			ou = userRepository.findByAdharNumber(request.getAdharNumber());
		}

		if (ou.isEmpty()) {
			throw new RuntimeException("Invalid Email or Mobile");
		}

		if (ou.isEmpty())
			throw new RuntimeException("Invalid credentials");

		User user = ou.get();
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
			throw new RuntimeException("Invalid credentials");

		if (!"ACTIVE".equalsIgnoreCase(user.getStatus()))
			throw new RuntimeException("User inactive");

		Map<String, Object> claims = new HashMap<>();
		claims.put("roleName", user.getRoleName());
		claims.put("permissions", user.getPermissions());
		claims.put("tenantId", user.getTenantId());
		claims.put("email", user.getEmail());
		claims.put("classId", user.getClassId());
		claims.put("userId", user.getUserId());
		String token = jwtTokenProvider.generateToken(user.getId(), claims);

		AuthResponse res = new AuthResponse();
		res.setAccessToken(token);
		res.setExpiresIn(3600);
		return res;
	}

	public User register(RegisterUserRequest req) {
		if (userRepository.existsByEmail(req.getEmail())) {
			throw new RuntimeException("Email already exists");
		}
		if (userRepository.existsByMobile(req.getMobile())) {
			throw new RuntimeException("Mobile already exists");
		}
		if (userRepository.existsByAdharNumber(req.getAdharNumber())) {
			throw new RuntimeException("Adhar Number already exists");
		}
		Role role = roleRepository.findByName(req.getRoleName())
				.orElseThrow(() -> new RuntimeException("Role not found: " + req.getRoleName()));

		User u = new User();
		u.setFullName(req.getFullName());
		u.setEmail(req.getEmail().toLowerCase());
		u.setPassword(passwordEncoder.encode(req.getPassword()));
		u.setMobile(req.getMobile());
		u.setStatus("ACTIVE");
		u.setRoleName(role.getName());
		u.setPermissions(role.getPermissions());
		u.setTenantId(req.getTenantId() != null ? req.getTenantId() : "default");
		u.setCreatedAt(Instant.now());
		u.setAdharNumber(req.getAdharNumber());
		u.setUserId(req.getUserId());
		userRepository.save(u);
		studentRepository.linkGuardianToIamUser(u.getAdharNumber(), u.getId());
		return u;
	}
}