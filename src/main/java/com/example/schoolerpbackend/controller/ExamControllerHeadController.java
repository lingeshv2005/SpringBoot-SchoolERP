package com.example.schoolerpbackend.controller;

import java.time.LocalDate;
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
import com.example.schoolerpbackend.entity.ExamControllerHead;
import com.example.schoolerpbackend.repository.ExamControllerHeadRepository;
import com.example.schoolerpbackend.repository.ExamRepository;

@RestController
@RequestMapping("/exam-controller-head")
public class ExamControllerHeadController {

    @Autowired
    private ExamRepository examRepository;

@Autowired
private ExamControllerHeadRepository examControllerHeadRepository;

@PostMapping("/create-exam")
public ResponseEntity<?> createExam(@RequestBody CreateExamRequest request, @RequestParam String ucId) {
    try {
        // Validate input
        if (request.getName() == null || request.getDate() == null) {
            return ResponseEntity.badRequest().body("Exam name and date are required");
        }
        if (ucId == null) {
            return ResponseEntity.badRequest().body("ucId is required");
        }

        Optional<ExamControllerHead> creator = examControllerHeadRepository.findByExamControllerHeadId(ucId);
        if (creator.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid ucId: ExamControllerHead not found");
        }

        // Create Exam
        Exam exam = new Exam();
        exam.setExamId(UUID.randomUUID().toString());
        exam.setName(request.getName());
        exam.setDate(request.getDate());
        exam.setCreatedBy(ucId);
        exam.setUpdatedBy(ucId);
        exam.setAssignedTimetables(new ArrayList<>());
        exam.setCreatedAt(LocalDateTime.now());
        exam.setUpdatedAt(LocalDateTime.now());

        Exam savedExam = examRepository.save(exam);

        // Add created exam to the creator's list
        ExamControllerHead head = creator.get();
        List<String> createdExams = head.getCreatedExams();
        if (createdExams == null) {
            createdExams = new ArrayList<>();
        }
        createdExams.add(savedExam.getExamId());
        head.setCreatedExams(createdExams);

        examControllerHeadRepository.save(head);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedExam);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error creating exam: " + e.getMessage());
    }
}

@PostMapping("/assign-timetable")
public ResponseEntity<?> assignTimetable(@RequestBody AssignTimetableRequest request,
                                         @RequestParam String ucId) {
    try {
        // Check if user (ExamControllerHead) exists
        Optional<ExamControllerHead> updater = examControllerHeadRepository.findByExamControllerHeadId(ucId);
        if (updater.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid ucId: ExamControllerHead not found");
        }

        // Find exam by examId
        Optional<Exam> optionalExam = examRepository.findByExamId(request.getExamId());
        if (optionalExam.isEmpty()) {
            return ResponseEntity.badRequest().body("Exam not found with examId: " + request.getExamId());
        }

        Exam exam = optionalExam.get();

        // Create new AssignedTimetable entry
        Exam.AssignedTimetable assigned = new Exam.AssignedTimetable();
        assigned.setTableUrl(request.getTableUrl());
        assigned.setMessage(request.getMessage());
        assigned.setStartDate(request.getStartDate());
        assigned.setEndDate(request.getEndDate());
        assigned.setApprovalStatus(false);
        assigned.setAssignedAt(LocalDateTime.now());
        assigned.setApprovedBy(null);

        // Add the new timetable to existing list or create new list
        List<Exam.AssignedTimetable> timetables = exam.getAssignedTimetables();
        if (timetables == null) {
            timetables = new ArrayList<>();
        }
        timetables.add(assigned);

        // Update exam entity
        exam.setAssignedTimetables(timetables);
        exam.setUpdatedBy(ucId);
        exam.setUpdatedAt(LocalDateTime.now());

        examRepository.save(exam);

        return ResponseEntity.ok("Timetable assigned successfully");

    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error assigning timetable: " + e.getMessage());
    }
}

    public static class CreateExamRequest {
        private String name;
        private LocalDate date;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }
    }

    public static class AssignTimetableRequest {
        private String examId;
        private String tableUrl;
        private String message;
        private LocalDate startDate;
        private LocalDate endDate;

        public String getExamId() {
            return examId;
        }

        public void setExamId(String examId) {
            this.examId = examId;
        }

        public String getTableUrl() {
            return tableUrl;
        }

        public void setTableUrl(String tableUrl) {
            this.tableUrl = tableUrl;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }
    }
}
