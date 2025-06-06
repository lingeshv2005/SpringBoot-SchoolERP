package com.example.schoolerpbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "principals", uniqueConstraints = {
    @UniqueConstraint(columnNames = "principalId"),
    @UniqueConstraint(columnNames = "user")
})
public class Principal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String principalId;

    @Column(nullable = false, unique = true)
    private String user;

    @ElementCollection
    @Column
    private List<String> approvedLeaves;

    @ElementCollection
    @Column
    private List<String> approvedExams;

    @ElementCollection
    @CollectionTable(name = "principal_disciplinary_actions")
    private List<DisciplinaryAction> managedDisciplinaryActions;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private String updatedBy;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Embeddable
    public static class DisciplinaryAction {
        private String student;
        private String action;
        private LocalDateTime date;
        private String description;

        // Getters and Setters
        public String getStudent() {
            return student;
        }

        public void setStudent(String student) {
            this.student = student;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public LocalDateTime getDate() {
            return date;
        }

        public void setDate(LocalDateTime date) {
            this.date = date;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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

    public String getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(String principalId) {
        this.principalId = principalId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public List<DisciplinaryAction> getManagedDisciplinaryActions() {
        return managedDisciplinaryActions;
    }

    public void setManagedDisciplinaryActions(List<DisciplinaryAction> managedDisciplinaryActions) {
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