package com.example.schoolerpbackend.controller;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

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
import com.example.schoolerpbackend.entity.Teacher;
import com.example.schoolerpbackend.entity.User;
import com.example.schoolerpbackend.repository.AdminRepository;
import com.example.schoolerpbackend.repository.TeacherRepository;
import com.example.schoolerpbackend.repository.UserRepository;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user, @RequestParam String ucId) {
        try {
            // Validate input
            if (user.getUsername() == null || user.getEmail() == null || user.getPassword() == null) {
                return ResponseEntity.badRequest().body("Username, email, and password are required");
            }
            if (ucId == null) {
                return ResponseEntity.badRequest().body("ucId is required");
            }

            // Check if username already exists
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
            }

            // Validate ucId exists in the database
            Optional<Admin> creator = adminRepository.findByAdminId(ucId);
            if (!creator.isPresent()) {
                return ResponseEntity.badRequest().body("Invalid ucId: Admin not found");
            }

            // Set createdBy and updatedBy to ucId
            user.setCreatedBy(ucId);
            user.setUpdatedBy(ucId);

            // Set default values and encrypt password
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            user.setActive(true);

            // Save user
            User savedUser = userRepository.save(user);

            if (user.getRoleName() == User.RoleName.TEACHER) {
                Teacher teacher = new Teacher();
                teacher.setTeacherId(UUID.randomUUID().toString());
                teacher.setUser(savedUser.getUserId()); // Link userId
                teacher.setCreatedBy(ucId);
                teacher.setUpdatedBy(ucId);
                teacher.setCreatedAt(LocalDateTime.now());
                teacher.setUpdatedAt(LocalDateTime.now());
                teacherRepository.save(teacher);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error registering user: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        try {
            // Validate input
            if (user.getUsername() == null || user.getPassword() == null) {
                return ResponseEntity.badRequest().body("Username and password are required");
            }

            // Find user by username
            Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
            if (!optionalUser.isPresent()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }

            User existingUser = optionalUser.get();

            // Check if user is active
            if (!existingUser.isActive()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User account is inactive");
            }

            // Verify password
            if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }

            // Return user details (or JWT token in a real application)
            return ResponseEntity.ok(existingUser);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error logging in: " + e.getMessage());
        }
    }
}