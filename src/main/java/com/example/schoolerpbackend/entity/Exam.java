package com.example.schoolerpbackend.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Document(collection = "exams")
public class Exam {

    @Id
    private String id;

    @NotBlank
    @Indexed(unique = true)
    private String examId = UUID.randomUUID().toString();

    @NotBlank
    private String name;

    @NotNull
    private LocalDate date;

    @NotBlank
    private String createdBy;

    private List<AssignedTimetable> assignedTimetables;

    @NotBlank
    private String updatedBy;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Exam() {
        this.examId = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Nested class for AssignedTimetable
    public static class AssignedTimetable {

        @NotBlank
        @Indexed(unique = true)
        private String assignedTimeTableId = UUID.randomUUID().toString();

        private String tableUrl;

        @NotBlank
        private String message;

        private LocalDateTime assignedAt = LocalDateTime.now();

        private Boolean approvalStatus = false;

        @NotNull
        private LocalDate startDate;

        @NotNull
        private LocalDate endDate;

        private String approvedBy;

        private LocalDateTime approvedAt;

        public AssignedTimetable() {
            this.assignedTimeTableId = UUID.randomUUID().toString();
            this.assignedAt = LocalDateTime.now();
            this.approvalStatus = false;
        }

        public LocalDateTime getApprovedAt() {
            return approvedAt;
        }

        public void setApprovedAt(LocalDateTime approvedAt) {
            this.approvedAt = approvedAt;
        }

        public String getAssignedTimeTableId() {
            return assignedTimeTableId;
        }

        public void setAssignedTimeTableId(String assignedTimeTableId) {
            this.assignedTimeTableId = assignedTimeTableId;
        }
        
        public String getTableUrl() {
            return tableUrl;
        }

        public void setTableUrl(String tableUrl) {
            this.tableUrl = tableUrl;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public LocalDateTime getAssignedAt() {
            return assignedAt;
        }

        public void setAssignedAt(LocalDateTime assignedAt) {
            this.assignedAt = assignedAt;
        }

        public Boolean getApprovalStatus() {
            return approvalStatus;
        }

        public void setApprovalStatus(Boolean approvalStatus) {
            this.approvalStatus = approvalStatus;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }

        public String getApprovedBy() {
            return approvedBy;
        }

        public void setApprovedBy(String approvedBy) {
            this.approvedBy = approvedBy;
        }
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
