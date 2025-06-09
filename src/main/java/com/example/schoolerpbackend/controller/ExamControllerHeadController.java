package com.example.schoolerpbackend.controller;

import java.time.LocalDate;
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

import com.example.schoolerpbackend.entity.Classroom;
import com.example.schoolerpbackend.entity.Exam;
import com.example.schoolerpbackend.entity.ExamControllerHead;
import com.example.schoolerpbackend.entity.ExamInvigilation;
import com.example.schoolerpbackend.entity.ExamResult;
import com.example.schoolerpbackend.entity.QuestionPaper;
import com.example.schoolerpbackend.repository.ClassroomRepository;
import com.example.schoolerpbackend.repository.ExamControllerHeadRepository;
import com.example.schoolerpbackend.repository.ExamInvigilationRepository;
import com.example.schoolerpbackend.repository.ExamRepository;
import com.example.schoolerpbackend.repository.ExamResultRepository;
import com.example.schoolerpbackend.repository.QuestionPaperRepository;
import com.example.schoolerpbackend.repository.SubjectRepository;

@RestController
@RequestMapping("/exam_controller_head")
public class ExamControllerHeadController {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
private ExamResultRepository examResultRepository;

@Autowired
private ClassroomRepository classroomRepository;

@Autowired
private SubjectRepository subjectRepository;

@Autowired
private ExamControllerHeadRepository examControllerHeadRepository;

@Autowired
private QuestionPaperRepository questionPaperRepository;

@Autowired
private ExamInvigilationRepository examInvigilationRepository;

@PostMapping("/create-exam")
public ResponseEntity<?> createExam(@RequestBody CreateExamRequest request, @RequestParam String ucId) {
    try {
        // Validate input
        if (request.getName() == null || request.getDate() == null) {
            return ResponseEntity.badRequest().body("Exam name and date are required");
        }
        if (ucId == null) {
            return ResponseEntity.badRequest().body("ucId is required");
        }

        Optional<ExamControllerHead> creator = examControllerHeadRepository.findByExamControllerHeadId(ucId);
        if (creator.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid ucId: ExamControllerHead not found");
        }

        // Create Exam
        Exam exam = new Exam();
        exam.setExamId(UUID.randomUUID().toString());
        exam.setName(request.getName());
        exam.setDate(request.getDate());
        exam.setCreatedBy(ucId);
        exam.setUpdatedBy(ucId);
        exam.setAssignedTimetables(new ArrayList<>());
        exam.setCreatedAt(LocalDateTime.now());
        exam.setUpdatedAt(LocalDateTime.now());

        Exam savedExam = examRepository.save(exam);

        // Add created exam to the creator's list
        ExamControllerHead head = creator.get();
        List<String> createdExams = head.getCreatedExams();
        if (createdExams == null) {
            createdExams = new ArrayList<>();
        }
        createdExams.add(savedExam.getExamId());
        head.setCreatedExams(createdExams);

        examControllerHeadRepository.save(head);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedExam);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error creating exam: " + e.getMessage());
    }
}

@PostMapping("/assign-timetable")
public ResponseEntity<?> assignTimetable(@RequestBody AssignTimetableRequest request,
                                         @RequestParam String ucId) {
    try {
        // Check if user (ExamControllerHead) exists
        Optional<ExamControllerHead> updater = examControllerHeadRepository.findByExamControllerHeadId(ucId);
        if (updater.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid ucId: ExamControllerHead not found");
        }

        // Find exam by examId
        Optional<Exam> optionalExam = examRepository.findByExamId(request.getExamId());
        if (optionalExam.isEmpty()) {
            return ResponseEntity.badRequest().body("Exam not found with examId: " + request.getExamId());
        }

        Exam exam = optionalExam.get();

        // Create new AssignedTimetable entry
        Exam.AssignedTimetable assigned = new Exam.AssignedTimetable();
        assigned.setTableUrl(request.getTableUrl());
        assigned.setMessage(request.getMessage());
        assigned.setStartDate(request.getStartDate());
        assigned.setEndDate(request.getEndDate());
        assigned.setApprovalStatus(false);
        assigned.setAssignedAt(LocalDateTime.now());
        assigned.setApprovedBy(null);

        // Add the new timetable to existing list or create new list
        List<Exam.AssignedTimetable> timetables = exam.getAssignedTimetables();
        if (timetables == null) {
            timetables = new ArrayList<>();
        }
        timetables.add(assigned);

        // Update exam entity
        exam.setAssignedTimetables(timetables);
        exam.setUpdatedBy(ucId);
        exam.setUpdatedAt(LocalDateTime.now());

        examRepository.save(exam);

        return ResponseEntity.ok("Timetable assigned successfully");

    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error assigning timetable: " + e.getMessage());
    }
}

@PostMapping("/assign-question-paper")
public ResponseEntity<?> applyQuestionPaperToInvigilations(@RequestBody AssignQuestionPaperRequest questionPaperRequest,
                                                           @RequestParam String ucId) {
    try {
        if (questionPaperRequest.getQuestionPaperId() == null || ucId == null) {
            return ResponseEntity.badRequest().body("questionPaperId and ucId are required");
        }

        Optional<ExamControllerHead> updater = examControllerHeadRepository.findByExamControllerHeadId(ucId);
        if (updater.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid ucId: ExamControllerHead not found");
        }

        Optional<QuestionPaper> optionalPaper = questionPaperRepository.findByQuestionPaperId(questionPaperRequest.getQuestionPaperId());
        if (optionalPaper.isEmpty()) {
            return ResponseEntity.badRequest().body("Question paper not found");
        }

        QuestionPaper paper = optionalPaper.get();

        if (!paper.getHodApproval()) {
            return ResponseEntity.badRequest().body("Question paper is not HOD-approved");
        }

        Optional<ExamInvigilation> optionalInvigilation = examInvigilationRepository
                .findByExamInvigilationId(questionPaperRequest.getExamInvigilationId());

        if (optionalInvigilation.isEmpty()) {
            return ResponseEntity.badRequest().body("Invigilation not found");
        }

        ExamInvigilation invigilation = optionalInvigilation.get();

        // Validate the exam match
        if (!invigilation.getExam().equalsIgnoreCase(paper.getExam())) {
            return ResponseEntity.badRequest().body("Exam mismatch between invigilation and question paper.");
        }

        boolean updated = false;

        for (ExamInvigilation.StudentGroup group : invigilation.getStudents()) {
            if (group.getSubject().equalsIgnoreCase(paper.getSubject())) {
                group.setQuestionPaperUrl(paper.getQuestionPaperUrl());
                updated = true;
            }
        }

        if (!updated) {
            return ResponseEntity.badRequest().body("No student group matched the question paper subject.");
        }

        invigilation.setApprovedBy(ucId);
        invigilation.setUpdatedBy(ucId);
        invigilation.setUpdatedAt(LocalDateTime.now());

        examInvigilationRepository.save(invigilation);

        return ResponseEntity.ok("Question paper applied successfully to relevant student groups.");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error applying question paper: " + e.getMessage());
    }
}




@PostMapping("/create-exam-result")
public ResponseEntity<?> createExamResult(@RequestBody CreateExamResultRequest request,
                                          @RequestParam String ucId) {
    try {
        // Validate request fields
        if (request.getExamId() == null || request.getClassroom() == null || request.getSubject() == null) {
            return ResponseEntity.badRequest().body("All fields (examId, classroom, subject) are required.");
        }

        // Validate ExamControllerHead
        Optional<ExamControllerHead> creatorOpt = examControllerHeadRepository.findByExamControllerHeadId(ucId);
        if (creatorOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid ucId: ExamControllerHead not found");
        }

        // Prevent duplicate result entry
        if (examResultRepository.existsByExamAndClassroomAndSubject(
                request.getExamId(), request.getClassroom(), request.getSubject())) {
            return ResponseEntity.badRequest().body("ExamResult already exists for this exam, classroom, and subject.");
        }

        // Validate subject
        if (subjectRepository.findBySubjectId(request.getSubject()).isEmpty()) {
            return ResponseEntity.badRequest().body("Subject not found.");
        }

        // Validate exam
        if (examRepository.findByExamId(request.getExamId()).isEmpty()) {
            return ResponseEntity.badRequest().body("Exam not found.");
        }

        // Validate classroom
        Optional<Classroom> optionalClassroom = classroomRepository.findByClassroomId(request.getClassroom());
        if (optionalClassroom.isEmpty()) {
            return ResponseEntity.badRequest().body("Classroom not found.");
        }

        Classroom classroom = optionalClassroom.get();

        // Fetch students from classroom
        List<String> studentIds = classroom.getStudents();
        if (studentIds == null || studentIds.isEmpty()) {
            return ResponseEntity.badRequest().body("No students found in this classroom.");
        }

        // Verify a teacher handles this subject in the classroom
        String teacherId = classroom.getSubjects().stream()
                .filter(st -> st.getSubject().equalsIgnoreCase(request.getSubject()))
                .map(Classroom.SubjectTeacher::getTeacher)
                .findFirst()
                .orElse(null);

        if (teacherId == null) {
            return ResponseEntity.badRequest().body("No teacher found for the subject in this classroom.");
        }

        // Create ExamResult
        ExamResult examResult = new ExamResult();
        examResult.setExamResultId(UUID.randomUUID().toString());
        examResult.setExam(request.getExamId());
        examResult.setClassroom(classroom.getClassroomId());
        examResult.setSubject(request.getSubject());
        examResult.setTeacher(teacherId);

        // Add students with empty grades
        List<ExamResult.StudentGrade> studentGrades = studentIds.stream().map(studentId -> {
            ExamResult.StudentGrade grade = new ExamResult.StudentGrade();
            grade.setStudent(studentId);
            grade.setGradeValue(0.0);
            grade.setComments(null);
            return grade;
        }).toList();

        examResult.setStudents(studentGrades);
        examResult.setApprovedBy(null); // To be filled when approved
        examResult.setCreatedBy(ucId);
        examResult.setUpdatedBy(ucId);
        examResult.setCreatedAt(LocalDateTime.now());
        examResult.setUpdatedAt(LocalDateTime.now());

        // Save to DB
        examResultRepository.save(examResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(examResult);

    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error creating exam result: " + e.getMessage());
    }
}


public static class CreateExamResultRequest {
    private String examId;
    private String classroom;
    private String subject;

    // Getters and setters
    public String getExamId() { return examId; }
    public void setExamId(String examId) { this.examId = examId; }

    public String getClassroom() { return classroom; }
    public void setClassroom(String classroom) { this.classroom = classroom; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

}

    public static class CreateExamRequest {
        private String name;
        private LocalDate date;

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
    }

    public static class AssignTimetableRequest {
        private String examId;
        private String tableUrl;
        private String message;
        private LocalDate startDate;
        private LocalDate endDate;

        public String getExamId() {
            return examId;
        }

        public void setExamId(String examId) {
            this.examId = examId;
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
    }

    public static class AssignQuestionPaperRequest {
    private String examInvigilationId;
    private String questionPaperId;

    public String getExamInvigilationId() {
        return examInvigilationId;
    }

    public void setExamInvigilationId(String examInvigilationId) {
        this.examInvigilationId = examInvigilationId;
    }

    public String getQuestionPaperId() {
        return questionPaperId;
    }

    public void setQuestionPaperId(String questionPaperId) {
        this.questionPaperId = questionPaperId;
    }

}

}
