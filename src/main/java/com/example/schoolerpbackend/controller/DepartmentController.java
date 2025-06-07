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
import com.example.schoolerpbackend.entity.Hod;
import com.example.schoolerpbackend.entity.Teacher;
import com.example.schoolerpbackend.repository.AdminRepository;
import com.example.schoolerpbackend.repository.DepartmentRepository;
import com.example.schoolerpbackend.repository.HodRepository;
import com.example.schoolerpbackend.repository.TeacherRepository;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private HodRepository hodRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createDepartment(@RequestBody Department department, @RequestParam String ucId) {
        try {
            // Basic validation
            if (department.getName() == null || department.getName().isEmpty()) {
                return ResponseEntity.badRequest().body("Department name is required");
            }

            if (ucId == null || ucId.isEmpty()) {
                return ResponseEntity.badRequest().body("ucId (creator ID) is required");
            }

            // Check if the name is already taken
            if (departmentRepository.findByName(department.getName()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Department name already exists");
            }

            // Validate the creator (ucId) exists
            Optional<Admin> creatorOpt = adminRepository.findByAdminId(ucId);
            if (creatorOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid ucId: Admin not found");
            }

            // Set metadata
            department.setCreatedBy(ucId);
            department.setUpdatedBy(ucId);
            department.setCreatedAt(LocalDateTime.now());
            department.setUpdatedAt(LocalDateTime.now());

            // Save
            Department saved = departmentRepository.save(department);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating department: " + e.getMessage());
        }
    }

    @PutMapping("/assignHod")
    public ResponseEntity<?> assignHodToDepartment(
            @RequestBody AssignHodRequest request,
            @RequestParam String ucId) {

        try {
            // Validate request body fields
            if (request.getHodId() == null || request.getDepartmentId() == null) {
                return ResponseEntity.badRequest().body("hodId and departmentId are required");
            }

            // Validate ucId
            Optional<Admin> ucAdminOpt = adminRepository.findByAdminId(ucId);
            if (!ucAdminOpt.isPresent()) {
                return ResponseEntity.badRequest().body("Invalid ucId: Admin not found");
            }

            // Validate HOD
            Optional<Hod> hodOpt = hodRepository.findByHodId(request.getHodId());
            if (!hodOpt.isPresent()) {
                return ResponseEntity.badRequest().body("HOD not found for hodId: " + request.getHodId());
            }

            // Validate Department
            Optional<Department> deptOpt = departmentRepository.findByDepartmentId(request.getDepartmentId());
            if (!deptOpt.isPresent()) {
                return ResponseEntity.badRequest().body("Department not found for departmentId: " + request.getDepartmentId());
            }

            Hod hod = hodOpt.get();
            Department department = deptOpt.get();

            // Update references
            hod.setDepartment(department.getDepartmentId());
            hod.setUpdatedBy(ucId);
            hod.setUpdatedAt(LocalDateTime.now());

            department.setHod(hod.getAdmin()); // Using adminId from HOD
            department.setUpdatedBy(ucId);
            department.setUpdatedAt(LocalDateTime.now());

            // Save updates
            hodRepository.save(hod);
            departmentRepository.save(department);

            return ResponseEntity.ok("HOD assigned to department successfully");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error assigning HOD: " + e.getMessage());
        }
    }

@PutMapping("/assignTeacher")
public ResponseEntity<?> assignTeacherToDepartment(
        @RequestBody AssignTeacherRequest request,
        @RequestParam String ucId) {

    try {
        // Validate inputs
        if (request.getTeacherId() == null || request.getDepartmentId() == null) {
            return ResponseEntity.badRequest().body("teacherId and departmentId are required");
        }

        // Check admin existence
        Optional<Admin> adminOpt = adminRepository.findByAdminId(ucId);
        if (adminOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid ucId: Admin not found");
        }

        // Check teacher existence
        Optional<Teacher> teacherOpt = teacherRepository.findByTeacherId(request.getTeacherId());
        if (teacherOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Teacher not found");
        }

        // Check department existence
        Optional<Department> deptOpt = departmentRepository.findByDepartmentId(request.getDepartmentId());
        if (deptOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Department not found");
        }

        Teacher teacher = teacherOpt.get();
        Department department = deptOpt.get();

        // Add department to teacher
        if (teacher.getDepartment() == null) {
            teacher.setDepartment(new java.util.ArrayList<>());
        }
        if (!teacher.getDepartment().contains(department.getDepartmentId())) {
            teacher.getDepartment().add(department.getDepartmentId());
        }

        // Add teacher to department
        if (department.getTeachers() == null) {
            department.setTeachers(new java.util.ArrayList<>());
        }
        if (!department.getTeachers().contains(teacher.getTeacherId())) {
            department.getTeachers().add(teacher.getTeacherId());
        }

        teacher.setUpdatedBy(ucId);
        teacher.setUpdatedAt(LocalDateTime.now());

        department.setUpdatedBy(ucId);
        department.setUpdatedAt(LocalDateTime.now());

        teacherRepository.save(teacher);
        departmentRepository.save(department);

        return ResponseEntity.ok("Teacher assigned to department successfully");

    } catch (Exception e) {
        return ResponseEntity.internalServerError().body("Error assigning teacher to department: " + e.getMessage());
    }
}


    // DTO for the request body
    public static class AssignHodRequest {
        private String hodId;
        private String departmentId;

        public String getHodId() {
            return hodId;
        }

        public void setHodId(String hodId) {
            this.hodId = hodId;
        }

        public String getDepartmentId() {
            return departmentId;
        }

        public void setDepartmentId(String departmentId) {
            this.departmentId = departmentId;
        }
    }

public static class AssignTeacherRequest {
    private String teacherId;
    private String departmentId;

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }
}

}
