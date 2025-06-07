package com.example.schoolerpbackend.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.schoolerpbackend.entity.Classroom;

public interface ClassroomRepository extends MongoRepository<Classroom, String> {
    
    Optional<Classroom> findByClassroomId(String classroomId);

    Optional<Classroom> findByName(String name);
}
