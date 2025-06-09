package com.example.schoolerpbackend.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.schoolerpbackend.entity.ExamResult;

@Repository
public interface ExamResultRepository extends MongoRepository<ExamResult, String> {
    Optional<ExamResult> findByExamResultId(String examResultId);
    boolean existsByExamAndClassroomAndSubject(String exam, String classroom, String subject);
}