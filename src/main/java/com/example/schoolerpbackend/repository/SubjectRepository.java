package com.example.schoolerpbackend.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.schoolerpbackend.entity.Subject;

public interface SubjectRepository extends MongoRepository<Subject, String> {
    Optional<Subject> findBySubjectId(String subjectId);
    Optional<Subject> findByCode(String code);
}
