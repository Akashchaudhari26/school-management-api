package com.sms.modules.iam.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sms.modules.iam.domain.Role;
import com.sms.modules.iam.domain.RoleName;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(RoleName name);
    Boolean existsByName(RoleName roleName);
    boolean existsByNameAndTenantId(RoleName roleName, String tenantId);
}
