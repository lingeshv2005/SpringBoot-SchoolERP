package com.example.schoolerpbackend.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.schoolerpbackend.entity.ExamControllerHead;

public interface ExamControllerHeadRepository extends MongoRepository<ExamControllerHead, String> {
    Optional<ExamControllerHead> findByExamControllerHeadId(String examControllerHeadId);
}
