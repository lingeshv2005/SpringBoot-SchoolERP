
package com.example.schoolerpbackend.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.schoolerpbackend.entity.ExamInvigilation;

public interface ExamInvigilationRepository extends MongoRepository<ExamInvigilation, String> {
    Optional<ExamInvigilation> findByExamInvigilationId(String examInvigilationId);
}
