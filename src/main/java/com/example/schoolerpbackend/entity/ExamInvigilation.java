package com.example.schoolerpbackend.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Document(collection = "exam_invigilations")
public class ExamInvigilation {

    @Id
    private String id;

    @NotBlank
    private String examInvigilationId = UUID.randomUUID().toString();

    @NotBlank
    private String exam; // reference to examId

    @NotBlank
    private String hall;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    @NotNull
    private List<StudentGroup> students;

    private List<String> invigilators;

    private String approvedBy;

    @NotBlank
    private String createdBy;

    @NotBlank
    private String updatedBy;

    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    private LocalDateTime updatedAt = LocalDateTime.now();

    public ExamInvigilation() {
        this.examInvigilationId = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Inner class for students array
    public static class StudentGroup {

        @NotBlank
        private String fromTo;

        @NotBlank
        private String subject;

        private int capacity = 0;

        private String questionPaperUrl;

        public String getFromTo() {
            return fromTo;
        }

        public void setFromTo(String fromTo) {
            this.fromTo = fromTo;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public int getCapacity() {
            return capacity;
        }

        public void setCapacity(int capacity) {
            this.capacity = capacity;
        }

        public String getQuestionPaperUrl() {
            return questionPaperUrl;
        }

        public void setQuestionPaperUrl(String questionPaperUrl) {
            this.questionPaperUrl = questionPaperUrl;
        }
    }

    // Getters and setters for outer class

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExamInvigilationId() {
        return examInvigilationId;
    }

    public void setExamInvigilationId(String examInvigilationId) {
        this.examInvigilationId = examInvigilationId;
    }

    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }

    public String getHall() {
        return hall;
    }

    public void setHall(String hall) {
        this.hall = hall;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public List<StudentGroup> getStudents() {
        return students;
    }

    public void setStudents(List<StudentGroup> students) {
        this.students = students;
    }

    public List<String> getInvigilators() {
        return invigilators;
    }

    public void setInvigilators(List<String> invigilators) {
        this.invigilators = invigilators;
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
