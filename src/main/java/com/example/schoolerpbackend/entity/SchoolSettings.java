package com.example.schoolerpbackend.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "school_settings", uniqueConstraints = {
    @UniqueConstraint(columnNames = "schoolSettingsId"),
    @UniqueConstraint(columnNames = "schoolSettingsSchema")
})
public class SchoolSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String schoolSettingsId;

    @Column(nullable = false, unique = true)
    private String schoolSettingsSchema;

    @Column(nullable = false)
    private String academicYear;

    @ElementCollection
    @Column
    private List<LocalDateTime> holidays;

    @ElementCollection
    @Column
    private List<String> timeTables;

    @ElementCollection
    private Map<String, Object> otherSettings;

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

    public String getSchoolSettingsId() {
        return schoolSettingsId;
    }

    public void setSchoolSettingsId(String schoolSettingsId) {
        this.schoolSettingsId = schoolSettingsId;
    }

    public String getSchoolSettingsSchema() {
        return schoolSettingsSchema;
    }

    public void setSchoolSettingsSchema(String schoolSettingsSchema) {
        this.schoolSettingsSchema = schoolSettingsSchema;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public List<LocalDateTime> getHolidays() {
        return holidays;
    }

    public void setHolidays(List<LocalDateTime> holidays) {
        this.holidays = holidays;
    }

    public List<String> getTimeTables() {
        return timeTables;
    }

    public void setTimeTables(List<String> timeTables) {
        this.timeTables = timeTables;
    }

    public Map<String, Object> getOtherSettings() {
        return otherSettings;
    }

    public void setOtherSettings(Map<String, Object> otherSettings) {
        this.otherSettings = otherSettings;
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