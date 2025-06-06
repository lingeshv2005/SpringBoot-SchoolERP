package com.example.schoolerpbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "exam_controller_heads", uniqueConstraints = {
    @UniqueConstraint(columnNames = "examControllerHeadId"),
    @UniqueConstraint(columnNames = "user")
})
public class ExamControllerHead {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String examControllerHeadId;

    @Column(nullable = false, unique = true)
    private String user;

    @ElementCollection
    @Column
    private List<String> createdExams;

    @ElementCollection
    @Column
    private List<String> approvedResults;

    @ElementCollection
    @CollectionTable(name = "exam_controller_head_invigilations")
    private List<ApprovedInvigilation> approvedInvigilations;

    @ElementCollection
    @CollectionTable(name = "exam_controller_head_question_papers")
    private List<CollectedQuestionPaper> collectedQuestionPapers;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private String updatedBy;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Embeddable
    public static class ApprovedInvigilation {
        private String examInvigilation;
        private LocalDateTime approvedAt;

        // Getters and Setters
        public String getExamInvigilation() {
            return examInvigilation;
        }

        public void setExamInvigilation(String examInvigilation) {
            this.examInvigilation = examInvigilation;
        }

        public LocalDateTime getApprovedAt() {
            return approvedAt;
        }

        public void setApprovedAt(LocalDateTime approvedAt) {
            this.approvedAt = approvedAt;
        }
    }

    @Embeddable
    public static class CollectedQuestionPaper {
        private String exam;
        private String questionPaper;

        // Getters and Setters
        public String getExam() {
            return exam;
        }

        public void setExam(String exam) {
            this.exam = exam;
        }

        public String getQuestionPaper() {
            return questionPaper;
        }

        public void setQuestionPaper(String questionPaper) {
            this.questionPaper = questionPaper;
        }
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExamControllerHeadId() {
        return examControllerHeadId;
    }

    public void setExamControllerHeadId(String examControllerHeadId) {
        this.examControllerHeadId = examControllerHeadId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<String> getCreatedExams() {
        return createdExams;
    }

    public void setCreatedExams(List<String> createdExams) {
        this.createdExams = createdExams;
    }

    public List<String> getApprovedResults() {
        return approvedResults;
    }

    public void setApprovedResults(List<String> approvedResults) {
        this.approvedResults = approvedResults;
    }

    public List<ApprovedInvigilation> getApprovedInvigilations() {
        return approvedInvigilations;
    }

    public void setApprovedInvigilations(List<ApprovedInvigilation> approvedInvigilations) {
        this.approvedInvigilations = approvedInvigilations;
    }

    public List<CollectedQuestionPaper> getCollectedQuestionPapers() {
        return collectedQuestionPapers;
    }

    public void setCollectedQuestionPapers(List<CollectedQuestionPaper> collectedQuestionPapers) {
        this.collectedQuestionPapers = collectedQuestionPapers;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}