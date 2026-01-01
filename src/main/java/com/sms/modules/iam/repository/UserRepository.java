package com.sms.modules.iam.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sms.modules.iam.domain.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByMobile(String mobile);
    boolean existsByAdharNumber(String adharNo);
    Optional<User> findByMobile(String mobile);
    Optional<User> findByAdharNumber(String adharNo);
    Optional<User> findByEmailOrMobile(String email, String mobile);
}
