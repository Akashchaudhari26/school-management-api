package com.sms.modules.iam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sms.modules.iam.domain.Role;
import com.sms.modules.iam.repository.RoleRepository;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role create(Role r) {
        return roleRepository.save(r);
    }

    public List<Role> list() {
        return roleRepository.findAll();
    }

    public Role update(String id, Role updated) {
        Role existing = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setPermissions(updated.getPermissions());
        existing.setDefault(updated.isDefault());
        return roleRepository.save(existing);
    }
}