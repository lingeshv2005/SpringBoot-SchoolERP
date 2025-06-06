package com.example.schoolerpbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "question_papers", uniqueConstraints = {
    @UniqueConstraint(columnNames = "questionPaperId")
})
public class QuestionPaper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String questionPaperId;

    @Column(nullable = false)
    private String paperFileUrl;

    @Column(nullable = false)
    private boolean hodApproval = false;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private String updatedBy;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

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

    public String getQuestionPaperId() {
        return questionPaperId;
    }

    public void setQuestionPaperId(String questionPaperId) {
        this.questionPaperId = questionPaperId;
    }

    public String getPaperFileUrl() {
        return paperFileUrl;
    }

    public void setPaperFileUrl(String paperFileUrl) {
        this.paperFileUrl = paperFileUrl;
    }

    public boolean isHodApproval() {
        return hodApproval;
    }

    public void setHodApproval(boolean hodApproval) {
        this.hodApproval = hodApproval;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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