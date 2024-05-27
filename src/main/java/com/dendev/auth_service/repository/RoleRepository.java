package com.dendev.auth_service.repository;

import com.dendev.auth_service.model.ERole;
import com.dendev.auth_service.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
