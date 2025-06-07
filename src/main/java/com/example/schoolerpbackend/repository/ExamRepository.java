package com.example.schoolerpbackend.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.schoolerpbackend.entity.Exam;

@Repository
public interface ExamRepository extends MongoRepository<Exam, String> {
    Optional<Exam> findByExamId(String examId);
}
