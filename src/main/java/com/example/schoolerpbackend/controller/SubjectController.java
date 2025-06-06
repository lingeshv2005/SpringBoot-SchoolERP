package com.example.schoolerpbackend.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.schoolerpbackend.entity.Admin;
import com.example.schoolerpbackend.entity.Subject;
import com.example.schoolerpbackend.repository.AdminRepository;
import com.example.schoolerpbackend.repository.SubjectRepository;

@RestController
@RequestMapping("/subject")
public class SubjectController {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private AdminRepository adminRepository;

    @PostMapping("/add")
    public ResponseEntity<?> addSubject(@RequestBody Subject subject, @RequestParam String ucId) {
        try {
            // Validate required input
            if (ucId == null || ucId.isEmpty()) {
                return ResponseEntity.badRequest().body("ucId (Admin ID) is required");
            }

            Optional<Admin> adminOptional = adminRepository.findByAdminId(ucId);
            if (!adminOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can add subjects");
            }

            // Set createdBy, updatedBy, createdAt, updatedAt
            subject.setCreatedBy(ucId);
            subject.setUpdatedBy(ucId);
            subject.setCreatedAt(LocalDateTime.now());
            subject.setUpdatedAt(LocalDateTime.now());

            // Save the subject
            Subject savedSubject = subjectRepository.save(subject);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSubject);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding subject: " + e.getMessage());
        }
    }
}
