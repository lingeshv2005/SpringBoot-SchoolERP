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
import com.example.schoolerpbackend.entity.Department;
import com.example.schoolerpbackend.repository.AdminRepository;
import com.example.schoolerpbackend.repository.DepartmentRepository;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private AdminRepository adminRepository;

    @PostMapping("/add")
    public ResponseEntity<?> addDepartment(@RequestBody Department department, @RequestParam String ucId) {
        try {
            // Validate ucId (must be a valid admin)
            if (ucId == null || ucId.isEmpty()) {
                return ResponseEntity.badRequest().body("ucId (Admin ID) is required");
            }

            Optional<Admin> adminOptional = adminRepository.findByAdminId(ucId);
            if (!adminOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can add departments");
            }

            // Set metadata fields
            department.setCreatedBy(ucId);
            department.setUpdatedBy(ucId);
            department.setCreatedAt(LocalDateTime.now());
            department.setUpdatedAt(LocalDateTime.now());

            // Save to DB
            Department savedDepartment = departmentRepository.save(department);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDepartment);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding department: " + e.getMessage());
        }
    }
}
