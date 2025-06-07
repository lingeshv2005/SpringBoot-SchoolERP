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

@Document(collection = "classrooms")
public class Classroom {

    @Id
    private String id;

    @NotBlank
    @Indexed(unique = true)
    private String classroomId = UUID.randomUUID().toString();

    @NotBlank
    private String createdBy;

    @NotBlank
    private String updatedBy;

    @NotBlank
    @Indexed(unique = true)
    private String name;

    private Integer capacity = 0;

    @NotBlank
    private String department;

    private String classTeacher;

    private List<SubjectTeacher> subjects;

    private List<Representative> representatives;

    @NotBlank
    private String batch;

    private String timeTable; // Represents file URL

    private List<String> students;

    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Classroom() {
        this.classroomId = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.capacity = 0;
    }

    public static class SubjectTeacher {
        private String subject;
        private String teacher;

        public SubjectTeacher() {
        }

        public SubjectTeacher(String subject, String teacher) {
            this.subject = subject;
            this.teacher = teacher;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getTeacher() {
            return teacher;
        }

        public void setTeacher(String teacher) {
            this.teacher = teacher;
        }
    }

    public static class Representative {
        private String typeOfRep;
        private String student;

        public Representative() {
        }

        public Representative(String typeOfRep, String student) {
            this.typeOfRep = typeOfRep;
            this.student = student;
        }

        public String getTypeOfRep() {
            return typeOfRep;
        }

        public void setTypeOfRep(String typeOfRep) {
            this.typeOfRep = typeOfRep;
        }

        public String getStudent() {
            return student;
        }

        public void setStudent(String student) {
            this.student = student;
        }
    }

    // Getters and Setters (Optional: remove if you don't want them)
    public String getId() {
        return id;
    }

    public String getClassroomId() {
        return classroomId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public String getName() {
        return name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public String getDepartment() {
        return department;
    }

    public String getClassTeacher() {
        return classTeacher;
    }

    public List<SubjectTeacher> getSubjects() {
        return subjects;
    }

    public List<Representative> getRepresentatives() {
        return representatives;
    }

    public String getBatch() {
        return batch;
    }

    public String getTimeTable() {
        return timeTable;
    }

    public List<String> getStudents() {
        return students;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setClassroomId(String classroomId) {
        this.classroomId = classroomId;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setClassTeacher(String classTeacher) {
        this.classTeacher = classTeacher;
    }

    public void setSubjects(List<SubjectTeacher> subjects) {
        this.subjects = subjects;
    }

    public void setRepresentatives(List<Representative> representatives) {
        this.representatives = representatives;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public void setTimeTable(String timeTable) {
        this.timeTable = timeTable;
    }

    public void setStudents(List<String> students) {
        this.students = students;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
