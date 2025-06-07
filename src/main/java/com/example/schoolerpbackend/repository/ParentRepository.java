package com.example.schoolerpbackend.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.schoolerpbackend.entity.Parent;

public interface ParentRepository extends MongoRepository<Parent, String> {
    Optional<Parent> findByParentId(String parentId);
    Optional<Parent> findByUser(String user);
}
