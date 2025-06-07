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

import com.example.schoolerpbackend.entity.Hod;
import com.example.schoolerpbackend.entity.QuestionPaper;
import com.example.schoolerpbackend.repository.DepartmentRepository;
import com.example.schoolerpbackend.repository.HodRepository;
import com.example.schoolerpbackend.repository.QuestionPaperRepository;

import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/hod")
public class HodController {


    @Autowired
    private HodRepository hodRepository;

    @Autowired
    private QuestionPaperRepository questionPaperRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @PostMapping("/approve-question-paper")
    public ResponseEntity<?> approveQuestionPaper(@RequestBody ApprovalRequest request,
                                                  @RequestParam String ucId) {
        try {
            // Check if HOD exists
            Optional<Hod> hodOpt = hodRepository.findByHodId(ucId);
            if (hodOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid ucId: HOD not found");
            }

            // Check if QuestionPaper exists
            Optional<QuestionPaper> qpOpt = questionPaperRepository.findByQuestionPaperId(request.getQuestionPaperId());
            if (qpOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Question paper not found");
            }

            // Update approval
            QuestionPaper qp = qpOpt.get();

            if (departmentRepository.findByDepartmentId(qp.getDepartment()).isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid department ID in question paper");
            }

            qp.setHodApproval(request.getApproval());
            qp.setUpdatedBy(ucId);
            qp.setUpdatedAt(LocalDateTime.now());

            QuestionPaper updatedQp = questionPaperRepository.save(qp);

            return ResponseEntity.ok(updatedQp);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating approval: " + e.getMessage());
        }
    }

    public static class ApprovalRequest {
        @NotBlank
        private String questionPaperId;
        private Boolean approval;

        public String getQuestionPaperId() {
            return questionPaperId;
        }

        public void setQuestionPaperId(String questionPaperId) {
            this.questionPaperId = questionPaperId;
        }

        public Boolean getApproval() {
            return approval;
        }

        public void setApproval(Boolean approval) {
            this.approval = approval;
        }
    }
}
 