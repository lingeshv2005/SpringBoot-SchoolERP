package com.example.schoolerpbackend.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.schoolerpbackend.entity.Teacher;

public interface TeacherRepository extends MongoRepository<Teacher, String> {
    Optional<Teacher> findByTeacherId(String teacherId);
}
