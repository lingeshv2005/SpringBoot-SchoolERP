package com.example.schoolerpbackend.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.schoolerpbackend.entity.Exam;
import com.example.schoolerpbackend.entity.ExamController;
import com.example.schoolerpbackend.entity.ExamInvigilation;
import com.example.schoolerpbackend.entity.Subject;
import com.example.schoolerpbackend.entity.Teacher;
import com.example.schoolerpbackend.repository.ExamControllerRepository;
import com.example.schoolerpbackend.repository.ExamInvigilationRepository;
import com.example.schoolerpbackend.repository.ExamRepository;
import com.example.schoolerpbackend.repository.SubjectRepository;
import com.example.schoolerpbackend.repository.TeacherRepository;

@RestController
@RequestMapping("/exam_controller")
public class ExamControllerController {

    @Autowired
    private ExamInvigilationRepository invigilationRepository;

    @Autowired
    private ExamControllerRepository examControllerRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ExamRepository examRepository;

@PostMapping("/create-invigilation")
public ResponseEntity<?> createInvigilation(@RequestBody ExamInvigilation invigilation,
                                            @RequestParam String ucId) {
    try {
        // Validate Exam Controller existence
        Optional<ExamController> controllerOpt = examControllerRepository.findByExamControllerId(ucId);
        if (controllerOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid examControllerId: User not found");
        }

        // Validate exam exists
        Optional<Exam> examOpt = examRepository.findByExamId(invigilation.getExam());
        if (examOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid examId: Exam not found");
        }

        Exam exam = examOpt.get();

        // Check if any assigned timetable is approved
        boolean hasApprovedTimetable = exam.getAssignedTimetables() != null &&
            exam.getAssignedTimetables().stream()
                .anyMatch(t -> Boolean.TRUE.equals(t.getApprovalStatus()));

        if (!hasApprovedTimetable) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Cannot create invigilation: No approved timetable for this exam.");
        }

        // Validate required fields
        if (invigilation.getHall() == null || invigilation.getHall().isEmpty() ||
            invigilation.getStartTime() == null || invigilation.getEndTime() == null ||
            invigilation.getStudents() == null || invigilation.getStudents().isEmpty()) {
            return ResponseEntity.badRequest().body("Missing required fields: hall, timings, students");
        }

        for (ExamInvigilation.StudentGroup studentGroup : invigilation.getStudents()) {
            if (studentGroup == null) {
                return ResponseEntity.badRequest().body("Invalid student group ID: " + studentGroup);
            }
            
            Optional<Subject> subject = subjectRepository.findBySubjectId(studentGroup.getSubject());
            if (subject.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid subject ID: " + studentGroup.getSubject());
            }
        }
for (String invigilator : invigilation.getInvigilators()) {
    if (invigilator == null || invigilator.isEmpty()) {
        return ResponseEntity.badRequest().body("Invalid invigilator ID: " + invigilator);
    }

    Optional<Teacher> teacher = teacherRepository.findByTeacherId(invigilator);
    if (teacher.isEmpty()) {
        return ResponseEntity.badRequest().body("Invalid invigilator ID: " + invigilator);
    }
}
        // Assign IDs and metadata
        invigilation.setExamInvigilationId(UUID.randomUUID().toString());
        invigilation.setCreatedBy(ucId);
        invigilation.setUpdatedBy(ucId);
        invigilation.setCreatedAt(LocalDateTime.now());
        invigilation.setUpdatedAt(LocalDateTime.now());

        ExamInvigilation saved = invigilationRepository.save(invigilation);

        // Link to exam controller
        ExamController controller = controllerOpt.get();
        List<String> assigned = controller.getAssignedInvigilations();
        if (assigned == null) {
            assigned = new ArrayList<>();
        }
        assigned.add(saved.getExamInvigilationId());
        controller.setAssignedInvigilations(assigned);
        examControllerRepository.save(controller);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);

    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error creating invigilation: " + e.getMessage());
    }
}
}
