package com.example.schoolerpbackend.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.schoolerpbackend.entity.Admin;
import com.example.schoolerpbackend.entity.Classroom;
import com.example.schoolerpbackend.entity.Student;
import com.example.schoolerpbackend.repository.AdminRepository;
import com.example.schoolerpbackend.repository.ClassroomRepository;
import com.example.schoolerpbackend.repository.StudentRepository;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private AdminRepository adminRepository;

    @PutMapping("/assignClassroom")
    public ResponseEntity<?> assignClassroomToStudent(
            @RequestBody AssignClassroomRequest request,
            @RequestParam String ucId) {

        try {
            // Validate input
            if (request.getStudentId() == null || request.getStudentId().isEmpty()) {
                return ResponseEntity.badRequest().body("studentId is required");
            }
            if (request.getClassroomId() == null || request.getClassroomId().isEmpty()) {
                return ResponseEntity.badRequest().body("classroomId is required");
            }
            if (ucId == null || ucId.isEmpty()) {
                return ResponseEntity.badRequest().body("ucId is required");
            }

            // Validate Admin
            Optional<Admin> adminOpt = adminRepository.findByAdminId(ucId);
            if (adminOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid ucId: Admin not found");
            }

            // Validate Student
            Optional<Student> studentOpt = studentRepository.findByStudentId(request.getStudentId());
            if (studentOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Student not found for studentId: " + request.getStudentId());
            }

            // Validate Classroom
            Optional<Classroom> classroomOpt = classroomRepository.findByClassroomId(request.getClassroomId());
            if (classroomOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Classroom not found for classroomId: " + request.getClassroomId());
            }

            Student student = studentOpt.get();
            student.setClassroom(request.getClassroomId());

            // Update metadata
            student.setUpdatedBy(ucId);
            student.setUpdatedAt(LocalDateTime.now());

            Classroom classroom = classroomOpt.get();
            if (classroom.getStudents() == null) {
                classroom.setStudents(new java.util.ArrayList<>());
            }
            classroom.getStudents().add(student.getStudentId());
            classroom.setUpdatedBy(ucId);
            classroom.setUpdatedAt(LocalDateTime.now());

            classroomRepository.save(classroom); // Save the updated classroom
            studentRepository.save(student);

            return ResponseEntity.ok("Classroom assigned to student successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error assigning classroom to student: " + e.getMessage());
        }
    }

    // DTO for the request body
    public static class AssignClassroomRequest {
        private String studentId;
        private String classroomId;

        public String getStudentId() {
            return studentId;
        }

        public void setStudentId(String studentId) {
            this.studentId = studentId;
        }

        public String getClassroomId() {
            return classroomId;
        }

        public void setClassroomId(String classroomId) {
            this.classroomId = classroomId;
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllClassrooms() {
        try {
            List<Classroom> classrooms = classroomRepository.findAll();
            return ResponseEntity.ok(classrooms);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching classrooms: " + e.getMessage());
        }
    }
}