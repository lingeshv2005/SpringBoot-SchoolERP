package com.example.schoolerpbackend.repository;

import com.example.schoolerpbackend.entity.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface AdminRepository extends MongoRepository<Admin, String> {
    Optional<Admin> findByAdminname(String adminname);
    Optional<Admin> findByEmail(String email);
    Optional<Admin> findByAdminId(String adminId);
}