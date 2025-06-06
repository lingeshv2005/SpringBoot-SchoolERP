package com.example.schoolerpbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "exam_controllers", uniqueConstraints = {
    @UniqueConstraint(columnNames = "examControllerId"),
    @UniqueConstraint(columnNames = "user")
})
public class ExamController {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String examControllerId;

    @Column(nullable = false, unique = true)
    private String user;

    @ElementCollection
    @CollectionTable(name = "exam_controller_invigilations")
    private List<AssignedInvigilation> assignedInvigilations;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private String updatedBy;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Embeddable
    public static class AssignedInvigilation {
        private String examInvigilation;
        private LocalDateTime assignedAt;

        // Getters and Setters
        public String getExamInvigilation() {
            return examInvigilation;
        }

        public void setExamInvigilation(String examInvigilation) {
            this.examInvigilation = examInvigilation;
        }

        public LocalDateTime getAssignedAt() {
            return assignedAt;
        }

        public void setAssignedAt(LocalDateTime assignedAt) {
            this.assignedAt = assignedAt;
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

    public List<AssignedInvigilation> getAssignedInvigilations() {
        return assignedInvigilations;
    }

    public void setAssignedInvigilations(List<AssignedInvigilation> assignedInvigilations) {
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