package com.example.schoolerpbackend.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.schoolerpbackend.entity.QuestionPaper;

@Repository
public interface QuestionPaperRepository extends MongoRepository<QuestionPaper, String> {
    
    Optional<QuestionPaper> findByQuestionPaperId(String questionPaperId);

}
