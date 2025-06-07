package com.example.schoolerpbackend.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;

@Document(collection = "exam_controllers")
public class ExamController {

    @Id
    private String id;

    @NotBlank
    @Indexed(unique = true)
    private String examControllerId = UUID.randomUUID().toString();

    @NotBlank
    @Indexed(unique = true)
    private String user; // Reference to User.userId

    private List<String> assignedInvigilations = new ArrayList<>();

    @NotBlank
    private String createdBy;

    @NotBlank
    private String updatedBy;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public ExamController() {
        this.examControllerId = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExamControllerId() {
        return examControllerId;
    }

    public void setExamControllerId(String examControllerId) {
        this.examControllerId = examControllerId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<String> getAssignedInvigilations() {
        return assignedInvigilations;
    }

    public void setAssignedInvigilations(List<String> assignedInvigilations) {
        this.assignedInvigilations = assignedInvigilations;
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
