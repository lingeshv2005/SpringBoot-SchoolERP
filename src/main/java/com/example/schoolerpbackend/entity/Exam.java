package com.example.schoolerpbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "exams", uniqueConstraints = {
    @UniqueConstraint(columnNames = "examId")
})
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String examId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column
    private String approvedBy;

    @Column(nullable = false)
    private String createdBy;

    @ElementCollection
    @CollectionTable(name = "exam_timetables")
    private List<AssignedTimetable> assignedTimetables;

    @Column(nullable = false)
    private String updatedBy;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Embeddable
    public static class AssignedTimetable {
        private String tableUrl;
        private LocalDateTime assignedAt;
        private boolean approvalStatus = false;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private String approvedBy;

        // Getters and Setters
        public String getTableUrl() {
            return tableUrl;
        }

        public void setTableUrl(String tableUrl) {
            this.tableUrl = tableUrl;
        }

        public LocalDateTime getAssignedAt() {
            return assignedAt;
        }

        public void setAssignedAt(LocalDateTime assignedAt) {
            this.assignedAt = assignedAt;
        }

        public boolean isApprovalStatus() {
            return approvalStatus;
        }

        public void setApprovalStatus(boolean approvalStatus) {
            this.approvalStatus = approvalStatus;
        }

        public LocalDateTime getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDateTime startDate) {
            this.startDate = startDate;
        }

        public LocalDateTime getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDateTime endDate) {
            this.endDate = endDate;
        }

        public String getApprovedBy() {
            return approvedBy;
        }

        public void setApprovedBy(String approvedBy) {
            this.approvedBy = approvedBy;
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

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<AssignedTimetable> getAssignedTimetables() {
        return assignedTimetables;
    }

    public void setAssignedTimetables(List<AssignedTimetable> assignedTimetables) {
        this.assignedTimetables = assignedTimetables;
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