package com.example.schoolerpbackend.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;

@Document(collection = "questionPapers")
public class QuestionPaper {

    @Id
    private String id;

    @NotBlank
    @Indexed(unique = true)
    private String questionPaperId = UUID.randomUUID().toString();

    @NotBlank
    private String questionPaperUrl;

    private Boolean hodApproval = false;

    @NotBlank
    private String message;

    @NotBlank
    private String department;

    @NotBlank
    private String subject;

    @NotBlank
    private String exam;

    @NotBlank
    private String createdBy;

    @NotBlank
    private String updatedBy;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public QuestionPaper() {
        this.questionPaperId = UUID.randomUUID().toString();
        this.hodApproval = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionPaperId() {
        return questionPaperId;
    }
    public void setQuestionPaperId(String questionPaperId) {
        this.questionPaperId = questionPaperId;
    }

    public String getQuestionPaperUrl() {
        return questionPaperUrl;
    }
    public void setQuestionPaperUrl(String questionPaperUrl) {
        this.questionPaperUrl = questionPaperUrl;
    }

    public Boolean getHodApproval() {
        return hodApproval;
    }
    public void setHodApproval(Boolean hodApproval) {
        this.hodApproval = hodApproval;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getExam() {
        return exam;
    }
    public void setExam(String exam) {
        this.exam = exam;
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
