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

@Document(collection = "hods")
public class Hod {

    @Id
    private String id;

    @NotBlank
    @Indexed(unique = true)
    private String hodId = UUID.randomUUID().toString();

    @NotBlank
    @Indexed(unique = true)
    private String admin; // Reference to Admin.adminId

    private String department; // Reference to Department.departmentId

    private List<String> approvedpapers;

    private List<String> approvedLeaves;

    private List<PerformanceReport> performanceReports;

    @NotBlank
    private String createdBy;

    @NotBlank
    private String updatedBy;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Hod() {
        this.hodId = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Inner class for performance report
    public static class PerformanceReport {
        private String clazz;
        private LocalDateTime generatedAt;
        private String fileUrl;

        public String getClazz() {
            return clazz;
        }

        public void setClazz(String clazz) {
            this.clazz = clazz;
        }

        public LocalDateTime getGeneratedAt() {
            return generatedAt;
        }

        public void setGeneratedAt(LocalDateTime generatedAt) {
            this.generatedAt = generatedAt;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHodId() {
        return hodId;
    }

    public void setHodId(String hodId) {
        this.hodId = hodId;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<String> getApprovedpapers() {
        return approvedpapers;
    }

    public void setApprovedpapers(List<String> approvedpapers) {
        this.approvedpapers = approvedpapers;
    }

    public List<String> getApprovedLeaves() {
        return approvedLeaves;
    }

    public void setApprovedLeaves(List<String> approvedLeaves) {
        this.approvedLeaves = approvedLeaves;
    }

    public List<PerformanceReport> getPerformanceReports() {
        return performanceReports;
    }

    public void setPerformanceReports(List<PerformanceReport> performanceReports) {
        this.performanceReports = performanceReports;
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
