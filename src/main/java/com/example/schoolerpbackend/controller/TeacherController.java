package com.example.schoolerpbackend.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.schoolerpbackend.entity.QuestionPaper;
import com.example.schoolerpbackend.entity.Subject;
import com.example.schoolerpbackend.entity.Teacher;
import com.example.schoolerpbackend.repository.ExamRepository;
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
}
