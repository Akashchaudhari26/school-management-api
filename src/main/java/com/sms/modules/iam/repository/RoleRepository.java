package com.sms.modules.iam.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sms.modules.iam.domain.Role;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(String name);
    Boolean existsByName(String roleName);
}
