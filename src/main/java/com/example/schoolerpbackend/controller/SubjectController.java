package com.example.schoolerpbackend.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.schoolerpbackend.entity.Admin;
import com.example.schoolerpbackend.entity.Department;
import com.example.schoolerpbackend.entity.Subject;
import com.example.schoolerpbackend.entity.Teacher;
import com.example.schoolerpbackend.repository.AdminRepository;
import com.example.schoolerpbackend.repository.DepartmentRepository;
import com.example.schoolerpbackend.repository.SubjectRepository;
import com.example.schoolerpbackend.repository.TeacherRepository;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createSubject(@RequestBody Subject subject, @RequestParam String ucId) {
        try {
            // Validate required fields
            if (subject.getName() == null || subject.getCode() == null || subject.getDepartment() == null) {
                return ResponseEntity.badRequest().body("Name, code, and department are required");
            }

            if (ucId == null || ucId.isEmpty()) {
                return ResponseEntity.badRequest().body("ucId (creator) is required");
            }

            // Validate creator
            Optional<Admin> creator = adminRepository.findByAdminId(ucId);
            if (creator.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid ucId: Admin not found");
            }

            if (subjectRepository.findByCode(subject.getCode()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Subject code already exists");
            }

            // Check department existence
            Optional<Department> deptOpt = departmentRepository.findByDepartmentId(subject.getDepartment());
            if (deptOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid departmentId: Department not found");
            }

            // Set metadata
            subject.setCreatedBy(ucId);
            subject.setUpdatedBy(ucId);
            subject.setCreatedAt(LocalDateTime.now());
            subject.setUpdatedAt(LocalDateTime.now());

            // Save and return
            Subject savedSubject = subjectRepository.save(subject);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSubject);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating subject: " + e.getMessage());
        }
    }

    
    @PutMapping("/assignTeacher")
    public ResponseEntity<?> assignTeacherToSubject(
            @RequestBody AssignSubjectTeacherRequest request,
            @RequestParam String ucId) {

        try {
            // Validate input
            if (request.getSubjectId() == null || request.getTeacherId() == null) {
                return ResponseEntity.badRequest().body("subjectId and teacherId are required");
            }

            if (ucId == null || ucId.isEmpty()) {
                return ResponseEntity.badRequest().body("ucId is required");
            }

            // Validate Admin
            Optional<Admin> adminOpt = adminRepository.findByAdminId(ucId);
            if (adminOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid ucId: Admin not found");
            }

            // Validate Subject
            Optional<Subject> subjectOpt = subjectRepository.findBySubjectId(request.getSubjectId());
            if (subjectOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Subject not found for subjectId: " + request.getSubjectId());
            }

            // Validate Teacher
            Optional<Teacher> teacherOpt = teacherRepository.findByTeacherId(request.getTeacherId());
            if (teacherOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Teacher not found for teacherId: " + request.getTeacherId());
            }

            Subject subject = subjectOpt.get();
            Teacher teacher = teacherOpt.get();

            // Check if teacher belongs to the subject's department
            if (teacher.getDepartment() == null || !teacher.getDepartment().contains(subject.getDepartment())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Teacher does not belong to the department of the subject");
            }

            // Update Subject → handlingTeachers
            if (subject.getHandlingTeachers() == null) {
                subject.setHandlingTeachers(new java.util.ArrayList<>());
            }
            if (!subject.getHandlingTeachers().contains(teacher.getTeacherId())) {
                subject.getHandlingTeachers().add(teacher.getTeacherId());
            }

            // Update Teacher → handlingSubjects
            if (teacher.getHandlingSubjects() == null) {
                teacher.setHandlingSubjects(new java.util.ArrayList<>());
            }
            if (!teacher.getHandlingSubjects().contains(subject.getSubjectId())) {
                teacher.getHandlingSubjects().add(subject.getSubjectId());
            }

            subject.setUpdatedBy(ucId);
            subject.setUpdatedAt(LocalDateTime.now());

            teacher.setUpdatedBy(ucId);
            teacher.setUpdatedAt(LocalDateTime.now());

            subjectRepository.save(subject);
            teacherRepository.save(teacher);

            return ResponseEntity.ok("Teacher assigned to subject successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error assigning teacher to subject: " + e.getMessage());
        }
    }

    // DTO class for request body
    public static class AssignSubjectTeacherRequest {
        private String subjectId;
        private String teacherId;

        public String getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(String subjectId) {
            this.subjectId = subjectId;
        }

        public String getTeacherId() {
            return teacherId;
        }

        public void setTeacherId(String teacherId) {
            this.teacherId = teacherId;
        }
    }
}
