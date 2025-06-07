package com.example.schoolerpbackend.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.schoolerpbackend.entity.Exam;
import com.example.schoolerpbackend.entity.Principal;
import com.example.schoolerpbackend.repository.ExamRepository;
import com.example.schoolerpbackend.repository.PrincipalRepository;

@RestController
@RequestMapping("/principal")
public class PrincipalController {

    @Autowired
    private PrincipalRepository principalRepository;

    @Autowired
    private ExamRepository examRepository;

    // Updated DTO to use assignedTimeTableId instead of index
    public static class ApproveTimetableRequest {
        private String examId;
        private String assignedTimeTableId;  // Use timetable ID now

        public String getExamId() {
            return examId;
        }

        public void setExamId(String examId) {
            this.examId = examId;
        }

        public String getAssignedTimeTableId() {
            return assignedTimeTableId;
        }

        public void setAssignedTimeTableId(String assignedTimeTableId) {
            this.assignedTimeTableId = assignedTimeTableId;
        }
    }

    @PostMapping("/approve-timetable")
    public ResponseEntity<?> approveTimetable(@RequestParam String ucId, @RequestBody ApproveTimetableRequest request) {
        try {
            // Validate principal
            Optional<Principal> principalOpt = principalRepository.findByPrincipalId(ucId);
            if (principalOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid principalId (ucId): Principal not found");
            }

            // Find the exam
            Optional<Exam> examOpt = examRepository.findByExamId(request.getExamId());
            if (examOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Exam not found with examId: " + request.getExamId());
            }

            Exam exam = examOpt.get();

            List<Exam.AssignedTimetable> timetables = exam.getAssignedTimetables();
            if (timetables == null || timetables.isEmpty()) {
                return ResponseEntity.badRequest().body("No assigned timetables found for this exam");
            }

            // Find the timetable by assignedTimeTableId
            Exam.AssignedTimetable timetableToApprove = timetables.stream()
                .filter(t -> t.getAssignedTimeTableId().equals(request.getAssignedTimeTableId()))
                .findFirst()
                .orElse(null);

            if (timetableToApprove == null) {
                return ResponseEntity.badRequest().body("Assigned timetable not found with id: " + request.getAssignedTimeTableId());
            }

            // Approve the timetable
            timetableToApprove.setApprovalStatus(true);
            timetableToApprove.setApprovedBy(ucId);
            timetableToApprove.setApprovedAt(LocalDateTime.now());

            // Save the updated exam
            examRepository.save(exam);

            // Optionally add the examId to the principal's approvedExams list
            Principal principal = principalOpt.get();
            List<String> approvedExams = principal.getApprovedExams();
            if (approvedExams == null) {
                approvedExams = new java.util.ArrayList<>();
            }
            if (!approvedExams.contains(exam.getExamId())) {
                approvedExams.add(exam.getExamId());
                principal.setApprovedExams(approvedExams);
                principalRepository.save(principal);
            }

            return ResponseEntity.ok("Timetable approved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error approving timetable: " + e.getMessage());
        }
    }
}
