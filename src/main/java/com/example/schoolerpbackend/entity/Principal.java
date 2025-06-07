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

@Document(collection = "principals")
public class Principal {

    @Id
    private String id;

    @NotBlank
    @Indexed(unique = true)
    private String principalId = UUID.randomUUID().toString();

    @NotBlank
    @Indexed(unique = true)
    private String admin;  // Reference to admin.adminId

    private List<String> approvedLeaves;

    private List<String> approvedExams;

    private List<String> managedDisciplinaryActions;

    @NotBlank
    private String createdBy;

    @NotBlank
    private String updatedBy;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Principal() {
        this.principalId = UUID.randomUUID().toString();
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

    public String getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(String principalId) {
        this.principalId = principalId;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public List<String> getApprovedLeaves() {
        return approvedLeaves;
    }

    public void setApprovedLeaves(List<String> approvedLeaves) {
        this.approvedLeaves = approvedLeaves;
    }

    public List<String> getApprovedExams() {
        return approvedExams;
    }

    public void setApprovedExams(List<String> approvedExams) {
        this.approvedExams = approvedExams;
    }

    public List<String> getManagedDisciplinaryActions() {
        return managedDisciplinaryActions;
    }

    public void setManagedDisciplinaryActions(List<String> managedDisciplinaryActions) {
        this.managedDisciplinaryActions = managedDisciplinaryActions;
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
