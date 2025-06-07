package com.example.schoolerpbackend.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;

@Document(collection = "exam_controller_heads")
public class ExamControllerHead {

    @Id
    private String id;

    @NotBlank
    @Indexed(unique = true)
    private String examControllerHeadId = UUID.randomUUID().toString();

    @NotBlank
    @Indexed(unique = true)
    private String admin; // Reference to Admin.adminId

    private List<String> createdExams; // References Exam.examId

    private List<String> approvedResults; // References ExamResult.resultId

    private List<String> approvedInvigilations;

    private List<String> collectedQuestionPapers;

    @NotBlank
    private String createdBy;

    @NotBlank
    private String updatedBy;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public ExamControllerHead() {
        this.examControllerHeadId = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters for main class fields

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExamControllerHeadId() {
        return examControllerHeadId;
    }

    public void setExamControllerHeadId(String examControllerHeadId) {
        this.examControllerHeadId = examControllerHeadId;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
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

    public List<String> getApprovedInvigilations() {
        return approvedInvigilations;
    }

    public void setApprovedInvigilations(List<String> approvedInvigilations) {
        this.approvedInvigilations = approvedInvigilations;
    }

    public List<String> getCollectedQuestionPapers() {
        return collectedQuestionPapers;
    }

    public void setCollectedQuestionPapers(List<String> collectedQuestionPapers) {
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
