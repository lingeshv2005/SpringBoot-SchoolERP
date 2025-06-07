package com.example.schoolerpbackend.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.schoolerpbackend.entity.Principal;

public interface PrincipalRepository extends MongoRepository<Principal, String> {
    Optional<Principal> findByPrincipalId(String principalId);
}
