package com.example.schoolerpbackend.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
import com.example.schoolerpbackend.entity.ExamController;
import com.example.schoolerpbackend.entity.Parent;
import com.example.schoolerpbackend.entity.Student;
import com.example.schoolerpbackend.entity.Teacher;
import com.example.schoolerpbackend.entity.User;
import com.example.schoolerpbackend.repository.AdminRepository;
import com.example.schoolerpbackend.repository.ExamControllerRepository;
import com.example.schoolerpbackend.repository.ParentRepository;
import com.example.schoolerpbackend.repository.TeacherRepository;
import com.example.schoolerpbackend.repository.UserRepository;
import com.example.schoolerpbackend.repository.StudentRepository;

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
    private StudentRepository studentRepository;

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private ExamControllerRepository examControllerRepository;

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

            // Register student if role is TEACHER
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

            // Register exam controller if role is EXAM_CONTROLLER
            if (user.getRoleName() == User.RoleName.EXAM_CONTROLLER) {
                ExamController examController = new ExamController();
                examController.setExamControllerId(UUID.randomUUID().toString());
                examController.setUser(savedUser.getUserId());
                examController.setCreatedBy(ucId);
                examController.setUpdatedBy(ucId);
                examController.setCreatedAt(LocalDateTime.now());
                examController.setUpdatedAt(LocalDateTime.now());
                examControllerRepository.save(examController);
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

    
@PostMapping("/register-student")
public ResponseEntity<?> registerStudent(@RequestBody RegisterStudentRequest request, @RequestParam String ucId) {
    try {
        User user = request.getUser();
        String admissionNumber = request.getAdmissionNumber();
        RegisterStudentRequest.ParentDTO parentDto = request.getParent();

        // Validation
        if (user.getUsername() == null || user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body("Username, email, and password are required");
        }
        if (admissionNumber == null || ucId == null) {
            return ResponseEntity.badRequest().body("admissionNumber and ucId are required");
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        Optional<Admin> creator = adminRepository.findByAdminId(ucId);
        if (creator.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid ucId: Admin not found");
        }

        // Save student user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedBy(ucId);
        user.setUpdatedBy(ucId);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setActive(true);
        user.setRoleName(User.RoleName.STUDENT);
        User savedUser = userRepository.save(user);

        // Create Student
        String studentId = UUID.randomUUID().toString();
        Student student = new Student();
        student.setStudentId(studentId);
        student.setUser(savedUser.getUserId());
        student.setAdmissionNumber(admissionNumber);
        student.setCreatedBy(ucId);
        student.setUpdatedBy(ucId);
        student.setCreatedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());

        // Handle parent
        if (parentDto != null) {
            Parent parent = null;

            if (parentDto.getParentId() != null) {
                parent = parentRepository.findByParentId(parentDto.getParentId()).orElse(null);
            }

            if (parent == null) {
                // Create new parent user
                User parentUser = new User();
                parentUser.setUsername(parentDto.getName()); // You can modify this logic
                parentUser.setEmail(parentDto.getEmail());
                parentUser.setPassword(passwordEncoder.encode(parentDto.getPassword()));
                parentUser.setCreatedBy(ucId);
                parentUser.setUpdatedBy(ucId);
                parentUser.setCreatedAt(LocalDateTime.now());
                parentUser.setUpdatedAt(LocalDateTime.now());
                parentUser.setActive(true);
                parentUser.setRoleName(User.RoleName.PARENT);
                User savedParentUser = userRepository.save(parentUser);

                // Create new parent entity
                parent = new Parent();
                parent.setParentId(parentDto.getParentId() != null ? parentDto.getParentId() : UUID.randomUUID().toString());
                parent.setUser(savedParentUser.getUserId());
                parent.setName(parentDto.getName());
                parent.setPhone(parentDto.getPhone());
                parent.setEmail(parentDto.getEmail());
                parent.setCreatedBy(ucId);
                parent.setUpdatedBy(ucId);
                parent.setCreatedAt(LocalDateTime.now());
                parent.setUpdatedAt(LocalDateTime.now());
                parent.setChildren(new ArrayList<>(List.of(studentId)));

                parentRepository.save(parent);
            } else {
                List<String> children = parent.getChildren();
                if (children == null) {
                    children = new ArrayList<>();
                }
                if (!children.contains(studentId)) {
                    children.add(studentId);
                    parent.setChildren(children);
                    parent.setUpdatedBy(ucId);
                    parent.setUpdatedAt(LocalDateTime.now());
                    parentRepository.save(parent);
                }
            }

            // Link parent to student
            student.setParent(parent.getParentId());
        }

        studentRepository.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);

    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error registering student: " + e.getMessage());
    }
}

    public static class RegisterStudentRequest {
        private User user;
        private String admissionNumber;
        private ParentDTO parent;
        private String ucId;

        public static class ParentDTO {
            private String parentId;
            private String password;
            private String name;
            private String phone;
            private String email;

            public String getParentId() {
                return parentId;
            }

            public void setParentId(String parentId) {
                this.parentId = parentId;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getAdmissionNumber() {
            return admissionNumber;
        }

        public void setAdmissionNumber(String admissionNumber) {
            this.admissionNumber = admissionNumber;
        }

        public ParentDTO getParent() {
            return parent;
        }

        public void setParent(ParentDTO parent) {
            this.parent = parent;
        }

        public String getUcId() {
            return ucId;
        }

        public void setUcId(String ucId) {
            this.ucId = ucId;
        }
    }
}
