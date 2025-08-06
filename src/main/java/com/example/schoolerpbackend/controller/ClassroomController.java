package com.example.schoolerpbackend.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.schoolerpbackend.entity.Admin;
import com.example.schoolerpbackend.entity.Classroom;
import com.example.schoolerpbackend.entity.Department;
import com.example.schoolerpbackend.entity.Subject;
import com.example.schoolerpbackend.entity.Teacher;
import com.example.schoolerpbackend.repository.AdminRepository;
import com.example.schoolerpbackend.repository.ClassroomRepository;
import com.example.schoolerpbackend.repository.DepartmentRepository;
import com.example.schoolerpbackend.repository.SubjectRepository;
import com.example.schoolerpbackend.repository.TeacherRepository;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/classrooms")
public class ClassroomController {

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private SubjectRepository subjectRepository;


@PostMapping("/create")
public ResponseEntity<?> createClassroom(@RequestBody Classroom classroom, @RequestParam String ucId) {
    try {
        // Check required request param
        if (ucId == null || ucId.isEmpty()) {
            return ResponseEntity.badRequest().body("ucId (creator ID) is required");
        }

        // Check required fields in the request body
        if (classroom.getName() == null || classroom.getName().isEmpty()) {
            return ResponseEntity.badRequest().body("Classroom name is required");
        }

        if (classroom.getDepartment() == null || classroom.getDepartment().isEmpty()) {
            return ResponseEntity.badRequest().body("Department is required");
        }

        if (classroom.getBatch() == null || classroom.getBatch().isEmpty()) {
            return ResponseEntity.badRequest().body("Batch is required");
        }

        // Check uniqueness of classroom name
        if (classroomRepository.findByName(classroom.getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Classroom name already exists");
        }

        // Check if admin exists for given ucId
        Optional<Admin> creatorOpt = adminRepository.findByAdminId(ucId);
        if (creatorOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid ucId: Admin not found");
        }

        Optional<Department> departmentOpt = departmentRepository.findByDepartmentId(classroom.getDepartment());
        if (departmentOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Department not found");
        }

        // Set metadata
        Integer capacity = classroom.getCapacity();
        classroom.setCapacity((capacity != null && capacity > 0) ? capacity : 0);
        classroom.setCreatedBy(ucId);
        classroom.setUpdatedBy(ucId);
        classroom.setCreatedAt(LocalDateTime.now());
        classroom.setUpdatedAt(LocalDateTime.now());

        // Save classroom
        Classroom saved = classroomRepository.save(classroom);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);

    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error creating classroom: " + e.getMessage());
    }
}

    @PutMapping("/assignClassTeacher")
    public ResponseEntity<?> assignClassTeacher(
            @RequestBody AssignClassTeacherRequest request,
            @RequestParam String ucId) {

        try {
            // Validate request
            if (request.getClassroomId() == null || request.getTeacherId() == null) {
                return ResponseEntity.badRequest().body("classroomId and teacherId are required");
            }

            if (ucId == null || ucId.isEmpty()) {
                return ResponseEntity.badRequest().body("ucId (updater ID) is required");
            }

            // Validate classroom existence
            Optional<Classroom> classroomOpt = classroomRepository.findByClassroomId(request.getClassroomId());
            if (classroomOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Classroom not found with id: " + request.getClassroomId());
            }

            // Validate teacher existence
            Optional<Teacher> teacherOpt = teacherRepository.findByTeacherId(request.getTeacherId());
            if (teacherOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Teacher not found with id: " + request.getTeacherId());
            }

            Classroom classroom = classroomOpt.get();
            Teacher teacher = teacherOpt.get();

            // Assign class teacher
            classroom.setClassTeacher(teacher.getTeacherId());
            teacher.setClassTeacher(classroom.getClassroomId());

            // Update audit fields
            classroom.setUpdatedBy(ucId);
            classroom.setUpdatedAt(LocalDateTime.now());
            teacher.setUpdatedBy(ucId);
            teacher.setUpdatedAt(LocalDateTime.now());

            // Save updates
            teacherRepository.save(teacher);
            classroomRepository.save(classroom);

            return ResponseEntity.ok("Class teacher assigned successfully");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error assigning class teacher: " + e.getMessage());
        }
    }

@Transactional
@PutMapping("/assignSubjects")
public ResponseEntity<?> assignSubjects(@RequestBody AssignSubjectsRequest request, @RequestParam String ucId) {
    try {
        if (request.getClassroomId() == null || request.getClassroomId().isEmpty()) {
            return ResponseEntity.badRequest().body("classroomId is required");
        }
        if (request.getSubjects() == null || request.getSubjects().isEmpty()) {
            return ResponseEntity.badRequest().body("At least one subject-teacher pair is required");
        }
        if (ucId == null || ucId.isEmpty()) {
            return ResponseEntity.badRequest().body("ucId (updater ID) is required");
        }

        Optional<Classroom> classroomOpt = classroomRepository.findByClassroomId(request.getClassroomId());
        if (classroomOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Classroom not found with id: " + request.getClassroomId());
        }
        Classroom classroom = classroomOpt.get();

        List<Classroom.SubjectTeacher> validSubjectTeacherPairs = new ArrayList<>();

        for (Classroom.SubjectTeacher st : request.getSubjects()) {
            String subjectId = st.getSubject();
            String teacherId = st.getTeacher();

            if (subjectId == null || subjectId.isEmpty() || teacherId == null || teacherId.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("Each subject-teacher pair must have both subject and teacherId");
            }

            // Validate teacher
            Optional<Teacher> teacherOpt = teacherRepository.findByTeacherId(teacherId);
            if (teacherOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Teacher not found with id: " + teacherId);
            }
            Teacher teacher = teacherOpt.get();

            // Validate subject
            Optional<Subject> subjectOpt = subjectRepository.findBySubjectId(subjectId);
            if (subjectOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Subject not found with id: " + subjectId);
            }
            Subject subject = subjectOpt.get();

            // Check teacher handles the subject
            if (teacher.getHandlingSubjects() == null || !teacher.getHandlingSubjects().contains(subjectId)) {
                return ResponseEntity.badRequest()
                        .body("Teacher with id " + teacherId + " does not handle subject: " + subjectId);
            }

            // Update teacher's handlingClasses (avoid duplicates)
            if (teacher.getHandlingClasses() == null) {
                teacher.setHandlingClasses(new ArrayList<>());
            }
            if (!teacher.getHandlingClasses().contains(classroom.getClassroomId())) {
                teacher.getHandlingClasses().add(classroom.getClassroomId());
                teacher.setUpdatedBy(ucId);
                teacher.setUpdatedAt(LocalDateTime.now());
                teacherRepository.save(teacher);
            }

            // Update subject's classes (avoid duplicates)
            if (subject.getClasses() == null) {
                subject.setClasses(new ArrayList<>());
            }
            if (!subject.getClasses().contains(classroom.getClassroomId())) {
                subject.getClasses().add(classroom.getClassroomId());
                subject.setUpdatedBy(ucId);
                subject.setUpdatedAt(LocalDateTime.now());
                subjectRepository.save(subject);
            }

            // Add to classroom subject list (de-duplicate later)
            validSubjectTeacherPairs.add(st);
        }

        // Remove duplicates from classroom's subject list
        List<Classroom.SubjectTeacher> deduplicatedSubjects = new ArrayList<>();
        Set<String> uniqueKeys = new HashSet<>();
        for (Classroom.SubjectTeacher st : validSubjectTeacherPairs) {
            String key = st.getSubject() + "_" + st.getTeacher(); // unique subject-teacher pair
            if (!uniqueKeys.contains(key)) {
                uniqueKeys.add(key);
                deduplicatedSubjects.add(st);
            }
        }

        // Save updated classroom
        classroom.setSubjects(deduplicatedSubjects);
        classroom.setUpdatedBy(ucId);
        classroom.setUpdatedAt(LocalDateTime.now());
        classroomRepository.save(classroom);

        return ResponseEntity.ok("Subjects assigned successfully to classroom " + classroom.getName());

    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error assigning subjects: " + e.getMessage());
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


    // DTO for assignClassTeacher request
    public static class AssignClassTeacherRequest {
        private String classroomId;
        private String teacherId;

        public String getClassroomId() {
            return classroomId;
        }

        public void setClassroomId(String classroomId) {
            this.classroomId = classroomId;
        }

        public String getTeacherId() {
            return teacherId;
        }

        public void setTeacherId(String teacherId) {
            this.teacherId = teacherId;
        }
    }
    // DTO for assigning subjects request
    public static class AssignSubjectsRequest {
        private String classroomId;
        private List<Classroom.SubjectTeacher> subjects;

        public String getClassroomId() {
            return classroomId;
        }

        public void setClassroomId(String classroomId) {
            this.classroomId = classroomId;
        }

        public List<Classroom.SubjectTeacher> getSubjects() {
            return subjects;
        }

        public void setSubjects(List<Classroom.SubjectTeacher> subjects) {
            this.subjects = subjects;
        }
    }

}
