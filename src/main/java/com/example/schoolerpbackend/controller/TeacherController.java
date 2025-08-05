package com.example.schoolerpbackend.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.schoolerpbackend.entity.ExamResult;
import com.example.schoolerpbackend.entity.QuestionPaper;
import com.example.schoolerpbackend.entity.Subject;
import com.example.schoolerpbackend.entity.Teacher;
import com.example.schoolerpbackend.repository.ExamRepository;
import com.example.schoolerpbackend.repository.ExamResultRepository;
import com.example.schoolerpbackend.repository.QuestionPaperRepository;
import com.example.schoolerpbackend.repository.SubjectRepository;
import com.example.schoolerpbackend.repository.TeacherRepository;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private QuestionPaperRepository questionPaperRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ExamResultRepository examResultRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @PostMapping("/create-question-paper")
    public ResponseEntity<?> createQuestionPaper(@RequestBody QuestionPaper questionPaper,
                                                 @RequestParam String ucId) {
        try {
            // Validate teacher existence
            Optional<Teacher> teacherOpt = teacherRepository.findByTeacherId(ucId);
            if (teacherOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid teacherId: Teacher not found");
            }
            Teacher teacher = teacherOpt.get();

            // Validate required fields presence and not empty
            if (questionPaper.getQuestionPaperUrl() == null || questionPaper.getQuestionPaperUrl().isEmpty() ||
                questionPaper.getMessage() == null || questionPaper.getMessage().isEmpty() ||
                questionPaper.getDepartment() == null || questionPaper.getDepartment().isEmpty() ||
                questionPaper.getSubject() == null || questionPaper.getSubject().isEmpty() ||
                questionPaper.getExam() == null || questionPaper.getExam().isEmpty()) {
                return ResponseEntity.badRequest().body("All fields are required");
            }

            // Check if exam exists in the system
            if(examRepository.findByExamId(questionPaper.getExam()).isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid examId: Exam not found");
            }

            // Check if teacher handles the subject
            List<String> handlingSubjects = teacher.getHandlingSubjects();
            if (handlingSubjects == null || !handlingSubjects.contains(questionPaper.getSubject())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Teacher does not handle the subject: " + questionPaper.getSubject());
            }

            // Validate subject exists and belongs to the department
            Optional<Subject> subjectOpt = subjectRepository.findBySubjectId(questionPaper.getSubject());
            if (subjectOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid subjectId: Subject not found");
            }

            Subject subject = subjectOpt.get();

            if (!subject.getDepartment().equals(questionPaper.getDepartment())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Subject does not belong to the specified department");
            }

            // Overwrite important fields
            questionPaper.setQuestionPaperId(UUID.randomUUID().toString());
            questionPaper.setCreatedBy(ucId);
            questionPaper.setUpdatedBy(ucId);
            questionPaper.setHodApproval(false);
            questionPaper.setCreatedAt(LocalDateTime.now());
            questionPaper.setUpdatedAt(LocalDateTime.now());

            // Save question paper
            QuestionPaper savedQp = questionPaperRepository.save(questionPaper);

            // Link question paper to teacher
            List<String> createdQuestionPapers = teacher.getCreatedQuestionPapers();
            if (createdQuestionPapers == null) {
                createdQuestionPapers = new ArrayList<>();
            }
            createdQuestionPapers.add(savedQp.getQuestionPaperId());
            teacher.setCreatedQuestionPapers(createdQuestionPapers);
            teacherRepository.save(teacher);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedQp);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating question paper: " + e.getMessage());
        }
    }

@PutMapping("/exam-result/update-marks")
public ResponseEntity<?> updateExamMarks(@RequestBody UpdateExamMarksRequest request,
                                         @RequestParam String ucId) {
    try {
        if (request.getExamResultId() == null || request.getUpdates() == null || request.getUpdates().isEmpty()) {
            return ResponseEntity.badRequest().body("ExamResultId and student updates are required.");
        }

        Optional<ExamResult> resultOpt = examResultRepository.findByExamResultId(request.getExamResultId());
        if (resultOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Exam result not found.");
        }

        ExamResult result = resultOpt.get();

        if (!ucId.equals(result.getTeacher())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You are not authorized to update this exam result.");
        }

        // Manually update student grades
        for (UpdateExamMarksRequest.StudentGradeUpdate update : request.getUpdates()) {
            for (ExamResult.StudentGrade studentGrade : result.getStudents()) {
                if (studentGrade.getStudent().equals(update.getStudentId())) {
                    studentGrade.setGradeValue(update.getGradeValue());
                    studentGrade.setComments(update.getComments());
                }
            }
        }

        result.setUpdatedBy(ucId);
        result.setUpdatedAt(LocalDateTime.now());
        examResultRepository.save(result);

        return ResponseEntity.ok("Exam marks updated successfully.");

    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error updating marks: " + e.getMessage());
    }
}

@GetMapping("/all")
public ResponseEntity<?> getAllTeachers() {
    try {
        List<Teacher> teachers = teacherRepository.findAll();
        return ResponseEntity.ok(teachers);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error fetching teachers: " + e.getMessage());
    }
}





public static class UpdateExamMarksRequest {
    private String examResultId;
    private List<StudentGradeUpdate> updates;

    public static class StudentGradeUpdate {
        private String studentId;
        private Double gradeValue;
        private String comments;

        // Getters and Setters
        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }

        public Double getGradeValue() { return gradeValue; }
        public void setGradeValue(Double gradeValue) { this.gradeValue = gradeValue; }

        public String getComments() { return comments; }
        public void setComments(String comments) { this.comments = comments; }
    }

    public String getExamResultId() { return examResultId; }
    public void setExamResultId(String examResultId) { this.examResultId = examResultId; }

    public List<StudentGradeUpdate> getUpdates() { return updates; }
    public void setUpdates(List<StudentGradeUpdate> updates) { this.updates = updates; }
}




}
