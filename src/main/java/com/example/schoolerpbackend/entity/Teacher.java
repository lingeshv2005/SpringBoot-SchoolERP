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
import jakarta.validation.constraints.NotNull;

@Document(collection = "teachers")
public class Teacher {

    @Id
    private String id;

    @NotBlank
    @Indexed(unique = true)
    private String teacherId = UUID.randomUUID().toString();

    @NotBlank
    private String createdBy;

    @NotBlank
    private String updatedBy;

    @NotBlank
    @Indexed(unique = true)
    private String user; // Reference to User.userId

    @NotNull
    private List<String> department; // References Department.departmentId

    private List<String> handlingSubjects; // References Subject.subjectId

    private String classTeacher; // References Class.classId

    private List<String> leaveRequests; // References LeaveRequest.leaveRequestId

    private List<String> handlingClasses; // References Class.classId

    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Teacher() {
        this.teacherId = UUID.randomUUID().toString();
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

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<String> getDepartment() {
        return department;
    }

    public void setDepartment(List<String> department) {
        this.department = department;
    }

    public List<String> getHandlingSubjects() {
        return handlingSubjects;
    }

    public void setHandlingSubjects(List<String> handlingSubjects) {
        this.handlingSubjects = handlingSubjects;
    }

    public String getClassTeacher() {
        return classTeacher;
    }

    public void setClassTeacher(String classTeacher) {
        this.classTeacher = classTeacher;
    }

    public List<String> getLeaveRequests() {
        return leaveRequests;
    }

    public void setLeaveRequests(List<String> leaveRequests) {
        this.leaveRequests = leaveRequests;
    }

    public List<String> getHandlingClasses() {
        return handlingClasses;
    }

    public void setHandlingClasses(List<String> handlingClasses) {
        this.handlingClasses = handlingClasses;
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
