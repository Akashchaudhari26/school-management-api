package com.sms.modules.iam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sms.modules.iam.domain.Role;
import com.sms.modules.iam.domain.RoleName;
import com.sms.modules.iam.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

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

    /**
     * Creates all default roles during first time application setup.
     */
    public void initializeDefaultRoles(String tenantId) {

        List<Role> roles = new ArrayList<>();

        addRoleIfMissing(roles,
                RoleName.SUPER_ADMIN,
                PermissionRegistry.all(),
                "Has complete access to the application.",
                tenantId);

        addRoleIfMissing(roles,
                RoleName.ADMIN,
                PermissionRegistry.admin(),
                "School Administrator.",
                tenantId);

        addRoleIfMissing(roles,
                RoleName.PRINCIPAL,
                PermissionRegistry.principal(),
                "School Principal.",
                tenantId);

        addRoleIfMissing(roles,
                RoleName.TEACHER,
                PermissionRegistry.teacher(),
                "Teacher.",
                tenantId);

        addRoleIfMissing(roles,
                RoleName.STUDENT,
                PermissionRegistry.student(),
                "Student.",
                tenantId);

        addRoleIfMissing(roles,
                RoleName.PARENT,
                PermissionRegistry.parent(),
                "Parent.",
                tenantId);

        addRoleIfMissing(roles,
                RoleName.ACCOUNTANT,
                PermissionRegistry.accountant(),
                "Accountant.",
                tenantId);

        addRoleIfMissing(roles,
                RoleName.RECEPTIONIST,
                PermissionRegistry.receptionist(),
                "Receptionist.",
                tenantId);

        roleRepository.saveAll(roles);
    }

    private void addRoleIfMissing(
            List<Role> roles,
            RoleName name,
            List<String> permissions,
            String description,
            String tenantId) {
        if (!roleRepository.existsByNameAndTenantId(name, tenantId)) {
            roles.add(createRole(name, permissions, description, tenantId));
        }
    }

    private Role createRole(
            RoleName name,
            List<String> permissions,
            String description,
            String tenantId) {

        Role role = new Role();

        role.setName(name);
        role.setDescription(description);
        role.setPermissions(permissions != null ? permissions : Collections.emptyList());
        role.setDefault(true);
        role.setTenantId(tenantId);

        return role;
    }
}
