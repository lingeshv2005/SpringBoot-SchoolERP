package com.example.schoolerpbackend.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.schoolerpbackend.entity.ExamController;

@Repository
public interface ExamControllerRepository extends MongoRepository<ExamController, String> {

    Optional<ExamController> findByExamControllerId(String examControllerId);

    Optional<ExamController> findByUser(String userId);
}
