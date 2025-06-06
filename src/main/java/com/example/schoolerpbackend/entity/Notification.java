package com.example.schoolerpbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "notifications", uniqueConstraints = {
    @UniqueConstraint(columnNames = "notificationId")
})
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String notificationId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String message;

    @ElementCollection
    @Column
    private List<String> recipients;

    @Column(nullable = false)
    private String sentBy;

    @ElementCollection
    @Column
    private List<String> readBy;

    @Column(nullable = false)
    private boolean important = false;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryMode deliveryMode = DeliveryMode.IN_APP;

    @ElementCollection
    @CollectionTable(name = "notification_all")
    private List<AllRecipient> all;

    @Column
    private LocalDateTime scheduledAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    public enum DeliveryMode {
        EMAIL, SMS, IN_APP
    }

    public enum Status {
        PENDING, SENT, FAILED
    }

    @Embeddable
    public static class AllRecipient {
        private boolean toAllHods = false;
        private boolean toAllParents = false;
        private boolean toAllTeachers = false;
        private boolean toAllStudents = false;
        private boolean toAllExaminationControllers = false;
        private boolean toAll = false;

        // Getters and Setters
        public boolean isToAllHods() {
            return toAllHods;
        }

        public void setToAllHods(boolean toAllHods) {
            this.toAllHods = toAllHods;
        }

        public boolean isToAllParents() {
            return toAllParents;
        }

        public void setToAllParents(boolean toAllParents) {
            this.toAllParents = toAllParents;
        }

        public boolean isToAllTeachers() {
            return toAllTeachers;
        }

        public void setToAllTeachers(boolean toAllTeachers) {
            this.toAllTeachers = toAllTeachers;
        }

        public boolean isToAllStudents() {
            return toAllStudents;
        }

        public void setToAllStudents(boolean toAllStudents) {
            this.toAllStudents = toAllStudents;
        }

        public boolean isToAllExaminationControllers() {
            return toAllExaminationControllers;
        }

        public void setToAllExaminationControllers(boolean toAllExaminationControllers) {
            this.toAllExaminationControllers = toAllExaminationControllers;
        }

        public boolean isToAll() {
            return toAll;
        }

        public void setToAll(boolean toAll) {
            this.toAll = toAll;
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

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }

    public List<String> getReadBy() {
        return readBy;
    }

    public void setReadBy(List<String> readBy) {
        this.readBy = readBy;
    }

    public boolean isImportant() {
        return important;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }

    public DeliveryMode getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(DeliveryMode deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public List<AllRecipient> getAll() {
        return all;
    }

    public void setAll(List<AllRecipient> all) {
        this.all = all;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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