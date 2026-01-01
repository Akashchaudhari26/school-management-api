package com.sms.modules.iam.service;

import org.springframework.beans.factory.annotation.Autowired;
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

    public List<User> listAll() {
        return userRepository.findAll();
    }

    public Optional<User> getById(String id) {
        return userRepository.findById(id);
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