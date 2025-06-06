package com.example.schoolerpbackend.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.schoolerpbackend.entity.Admin;
import com.example.schoolerpbackend.repository.AdminRepository;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerAdmin(@RequestBody Admin admin, @RequestParam String ucId) {
        try {
            // Validate input
            if (admin.getAdminname() == null || admin.getEmail() == null || admin.getPassword() == null) {
                return ResponseEntity.badRequest().body("Adminname, email, and password are required");
            }
            if (ucId == null) {
                return ResponseEntity.badRequest().body("ucId is required");
            }

            // Check if adminname already exists
            if (adminRepository.findByAdminname(admin.getAdminname()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Adminname already exists");
            }

            // Validate ucId exists in the admins collection
            Optional<Admin> creator = adminRepository.findByAdminId(ucId);
            if (!creator.isPresent()) {
                return ResponseEntity.badRequest().body("Invalid ucId: Admin not found");
            }

            // Set createdBy and updatedBy to ucId
            admin.setCreatedBy(ucId);
            admin.setUpdatedBy(ucId);

            // Set default values and encrypt password
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());
            admin.setActive(true); 

            // Save admin
            Admin savedAdmin = adminRepository.save(admin);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAdmin);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error registering admin: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@RequestBody Admin admin) {
        try {
            // Validate input
            if (admin.getAdminname() == null || admin.getPassword() == null) {
                return ResponseEntity.badRequest().body("Adminname and password are required");
            }

            // Find admin by adminname
            Optional<Admin> optionalAdmin = adminRepository.findByAdminname(admin.getAdminname());
            if (!optionalAdmin.isPresent()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid adminname or password");
            }

            Admin existingAdmin = optionalAdmin.get();

            // Check if admin is active
            if (!existingAdmin.isActive()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin account is inactive");
            }

            // Verify password
            if (!passwordEncoder.matches(admin.getPassword(), existingAdmin.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid adminname or password");
            }

            // Return admin details (or JWT token in a real application)
            return ResponseEntity.ok(existingAdmin);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error logging in: " + e.getMessage());
        }
    }
}