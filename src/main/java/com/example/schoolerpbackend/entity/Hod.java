package com.example.schoolerpbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "hods", uniqueConstraints = {
    @UniqueConstraint(columnNames = "hodId"),
    @UniqueConstraint(columnNames = "user")
})
public class Hod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String hodId;

    @Column(nullable = false, unique = true)
    private String user;

    @Column(nullable = false)
    private String department;

    @ElementCollection
    @Column
    private List<String> approvedPapers;

    @ElementCollection
    @Column
    private List<String> approvedLeaves;

    @ElementCollection
    @CollectionTable(name = "hod_performance_reports")
    private List<PerformanceReport> performanceReports;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private String updatedBy;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Embeddable
    public static class PerformanceReport {
        private String className;
        private LocalDateTime generatedAt;
        private String fileUrl;

        // Getters and Setters
        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
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

    public String getHodId() {
        return hodId;
    }

    public void setHodId(String hodId) {
        this.hodId = hodId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<String> getApprovedPapers() {
        return approvedPapers;
    }

    public void setApprovedPapers(List<String> approvedPapers) {
        this.approvedPapers = approvedPapers;
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