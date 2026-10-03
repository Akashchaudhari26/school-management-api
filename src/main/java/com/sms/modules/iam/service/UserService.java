package com.sms.modules.iam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sms.modules.iam.domain.User;
import com.sms.modules.iam.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> listAll() {
        return userRepository.findAll();
    }

    public Optional<User> getById(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User create(User u) {
        u.setCreatedAt(Instant.now());
        return userRepository.save(u);
    }

    public User update(String id, User updated) {
        User ex = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        ex.setFullName(updated.getFullName());
        ex.setMobile(updated.getMobile());
        ex.setProfileImageUrl(updated.getProfileImageUrl());
        ex.setUpdatedAt(Instant.now());
        ex.setEmail(updated.getEmail());
        if (updated.getPassword() != null && !updated.getPassword().isBlank()) {
            ex.setPassword(passwordEncoder.encode(updated.getPassword()));
        }
        return userRepository.save(ex);
    }

    public void delete(String id) {
        userRepository.deleteById(id);
    }

    public void changeStatus(String id, String status) {
        User u = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        u.setStatus(status);
        u.setUpdatedAt(Instant.now());
        userRepository.save(u);
    }
}
