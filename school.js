// ✅ User Schema
const userSchema = new Schema({
  userId: { type: String, required: true, unique: true },
  username: { type: String, required: true, unique: true },
  password: { type: String, required: true },
  email: { type: String, required: true },
  phoneNumber: { type: String, required:true },
  roleName: {
    type: String,
    enum: ['TEACHER', 'STUDENT', 'PARENT', 'EXAM_CONTROLLER'],
    required: true
  },
  alsoRole: { 
    type: String, 
    enum: ['ADMIN', 'PRINCIPAL', 'HOD', 'EXAM_CONTROLLER_HEAD']
  },
  isActive: { type: Boolean, default: true },
  createdBy: { type: String, required: true },
  updatedBy: { type: String, required: true }
}, { timestamps: true });

// ✅ Admin Schema
const adminSchema = new Schema({
  adminId: { type: String, required: true, unique: true },
  adminname: { type: String, required: true, unique: true },
  password: { type: String, required: true },
  email: { type: String, required: true },
  phoneNumber: { type: String },
  roleName: {
    type: String,
    enum: ['ADMIN', 'PRINCIPAL', 'HOD', 'EXAM_CONTROLLER_HEAD'], 
    required: true
  },
  isActive: { type: Boolean, default: true },
  createdBy: { type: String, required: true },
  updatedBy: { type: String, required: true }
}, { timestamps: true });

// ✅ School Settings Schema
const schoolSettingsSchema = new Schema({
  schoolSettingsId: { type: String, required: true, unique: true },
  schoolSettingsSchema: { type: String, required: true, unique: true },
  academicYear: { type: String, required: true },
  holidays: [{ type: Date }],
  timeTables: [{ type: String }],
  otherSettings: { type: Schema.Types.Mixed },
  createdBy: { type: String, required: true },
  updatedBy: { type: String, required: true }
}, { timestamps: true });

// ✅ Classroom Schema
const classroomSchema = new Schema({
  classroomId: { type: String, required: true, unique: true },
  createdBy: { type: String, required: true },
  updatedBy: { type: String, required: true },
  name: { type: String, required: true, unique: true },
  capacity: { type: Number, default: 0 },
  department: { type: String, required: true },
  classTeacher: { type: String },
  subjects: [{
    subject: { type: String },
    teacher: { type: String },
  }],
  representatives: [{
    typeOfRep: { type: String },
    student: { type: String }
  }],
  batch: { type: String, required: true },
  timeTable: { type: String },
  students: [{ type: String }]
}, { timestamps: true });

// ✅ Department Schema
const departmentSchema = new Schema({
  departmentId: { type: String, required: true, unique: true },
  createdBy: { type: String, required: true },
  updatedBy: { type: String, required: true },
  name: { type: String, required: true, unique: true },
  hod: { type: String },
  teachers: [{ type: String }]
}, { timestamps: true });

// ✅ Subject Schema
const subjectSchema = new Schema({
  subjectId: { type: String, required: true, unique: true },
  createdBy: { type: String, required: true },
  updatedBy: { type: String, required: true },
  name: { type: String, required: true },
  code: { type: String, required: true, unique: true },
  department: { type: String, required: true },
  handlingTeachers: [{ type: String }],
  classes: [{ type: String }]
}, { timestamps: true });

// ✅ Teacher Schema
const teacherSchema = new Schema({
  teacherId: { type: String, required: true, unique: true },
  createdBy: { type: String, required: true },
  updatedBy: { type: String, required: true },
  user: { type: String, required: true, unique: true },
  department: [{ type: String, required: true }],
  handlingSubjects: [{ type: String }],
  classTeacher: { type: String },
  leaveRequests: [{ type: String }]
}, { timestamps: true });

// ✅ Student Schema
const studentSchema = new Schema({
  studentId: { type: String, required: true, unique: true },
  createdBy: { type: String, required: true },
  updatedBy: { type: String, required: true },
  user: { type: String, required: true, unique: true },
  classroom: { type: String },
  parent: { type: String },
  admissionNumber: { type: String, required: true, unique: true },
  dateOfBirth: { type: Date },
  attendanceRecord: [{ type: String }],
  others: { type: Schema.Types.Mixed }
}, { timestamps: true });

// ✅ Parent Schema
const parentSchema = new Schema({
  parentId: { type: String, required: true, unique: true },
  user: { type: String, required: true, unique: true },
  name: { type: String, required: true },
  phone: { type: String, required: true },
  email: { type: String },
  children: [{ type: String }],
  notifications: [{ type: String }],
  createdBy: { type: String, required: true },
  updatedBy: { type: String, required: true }
}, { timestamps: true });

// ✅ Principal Schema
const principalSchema = new Schema({
  principalId: { type: String, required: true, unique: true },
  admin: { type: String, required: true, unique: true },
  approvedLeaves: [{ type: String }],
  approvedExams: [{ type: String }],
  managedDisciplinaryActions: [{ type: String }],
  createdBy: { type: String, required: true },
  updatedBy: { type: String, required: true }
}, { timestamps: true });

// ✅ HOD Schema
const hodSchema = new Schema({
  hodId: { type: String, required: true, unique: true },
  admin: { type: String, required: true, unique: true },
  department: { type: String },
  approvedpapers: [{ type: String }],
  approvedLeaves: [{ type: String }],
  performanceReports: [{
    class: { type: String },
    generatedAt: { type: Date },
    fileUrl: { type: String }
  }],
  createdBy: { type: String, required: true },
  updatedBy: { type: String, required: true }
}, { timestamps: true });

// ✅ Exam Controller Schema
const examControllerSchema = new Schema({
  examControllerId: { type: String, required: true, unique: true },
  user: { type: String, required: true, unique: true },
  assignedInvigilations: [{
    examInvigilation: { type: String },
    assignedAt: { type: Date, default: Date.now }
  }],
  createdBy: { type: String, required: true },
  updatedBy: { type: String, required: true}
}, { timestamps: true });

// ✅ Exam Controller Head Schema
const examControllerHeadSchema = new Schema({
  examControllerHeadId: { type: String, required: true, unique: true },
  admin: { type: String, required: true, unique: true },
  createdExams: [{ type: String }],
  approvedResults: [{ type: String }],
  approvedInvigilations: [{
    examInvigilation: { type: String },
    approvedAt: { type: Date, default: Date.now }
  }],
  collectedQuestionPapers: [{
    exam: { type: String },
    questionPaper: { type: String }
  }],
  createdBy: { type: String, required: true },
  updatedBy: { type: String, required: true }
}, { timestamps: true });

// ✅ Question Paper Schema
const questionPaperSchema = new Schema({
  questionPaperId: { type: String, required: true, unique: true },
  questionPaperUrl: { type: String, required: true },
  hodApproval: { type: Boolean, default: false },
  message: { type: String, required: true },
  department: { type: String, required: true },
  subject: { type: String, required: true },
  exam: { type: String, required: true },
  createdBy: { type: String, required: true },
  updatedBy: { type: String, required: true },
}, { timestamps: true });

// ✅ Exam Schema
const examSchema = new Schema({
  examId: { type: String, required: true, unique: true },
  name: { type: String, required: true },
  date: { type: Date, required: true },
  approvedBy: { type: String },
  createdBy: { type: String, required: true },
  assignedTimetables: [{
    tableUrl: { type: String },
    message: { type: String, required: true },
    assignedAt: { type: Date, default: Date.now },
    approvalStatus: { type: Boolean, default: false },
    startDate: { type: Date, required: true },
    endDate: { type: Date, required: true },
    approvedBy: { type: String, default: null }
  }],
  updatedBy: { type: String, required: true },
}, { timestamps: true });

// ✅ Exam Invigilation Schema
const examInvigilationSchema = new Schema({
  examInvigilationId: { type: String, required: true, unique: true },
  exam: { type: String, required: true },
  hall: { type: String, required: true },
  startTime: { type: Date, required: true },
  endTime: { type: Date, required: true },
  students: [{
    fromTo: { type: String, required: true },
    subject: { type: String, required: true },
    capacity: { type: Number, default: 0 },
    questionPaperUrl: { type: String }
  }],
  invigilators: [{ type: String }],
  approvedBy: { type: String },
  createdBy: { type: String, required: true },
  updatedBy: { type: String, required: true },
}, { timestamps: true });

// ✅ Exam Result Schema
const examResultSchema = new Schema({
  examResultId: { type: String, required: true, unique: true },
  exam: { type: String, required: true },
  classroom: { type: String, required: true },
  subject: { type: String, required: true },
  students: [{
    student: { type: String, required: true },
    gradeValue: { type: String, required: true },
    comments: { type: String }
  }],
  approvedBy: { type: String },
  updatedBy: { type: String, required: true },
  createdBy: { type: String, required: true }
}, { timestamps: true });


// ✅ Leave Request Schema
const leaveRequestSchema = new Schema({
  leaveRequestId: { type: String, required: true, unique: true },
  user: { type: String, required: true },
  startDate: { type: Date, required: true },
  endDate: { type: Date, required: true },
  status: {
    type: String,
    enum: ['PENDING', 'APPROVED', 'REJECTED'],
    default: 'PENDING'
  },
  reason: { type: String },
  fileUrl: { type: String },
  to: { type: String },
  approvedBy: { type: String },
  createdBy: { type: String, required: true },
  updatedBy: { type: String, required: true }
}, { timestamps: true });

// ✅ Attendance Record Schema
const attendanceRecordSchema = new Schema({
  attendanceRecordId: { type: String, required: true, unique: true },
  student: { type: String, required: true },
  date: { type: Date, required: true },
  status: {
    type: String,
    enum: ['PRESENT', 'ABSENT', 'LATE'],
    required: true
  },
  markedBy: { type: String, required: true },
  class: { type: String },
  createdBy: { type: String, required: true },
  updatedBy: { type: String, required: true }
}, { timestamps: true });
attendanceRecordSchema.index({ student: 1, date: 1 }, { unique: true });

// ✅ Discipline Record Schema
const disciplineRecordSchema = new Schema({
  disciplineRecordId: { type: String, required: true, unique: true },
  student: { type: String, required: true },
  description: { type: String, required: true },
  date: { type: Date, default: Date.now },
  reportedBy: { type: String, required: true },
  createdBy: { type: String, required: true },
  updatedBy: { type: String, required: true }
}, { timestamps: true });

// ✅ Notification Schema
const notificationSchema = new Schema({
  notificationId: { type: String, required: true, unique: true },
  title: { type: String, required: true },
  message: { type: String, required: true },
  recipients: [{ type: String }],
  sentBy: { type: String, required: true },
  readBy: [{ type: String }],
  important: { type: Boolean, default: false },
  deliveryMode: {
    type: String,
    enum: ['EMAIL', 'SMS', 'IN_APP'],
    default: 'IN_APP'
  },
  all: [{
    toAllHods: { type: Boolean, default: false },
    toAllParents: { type: Boolean, default: false },
    toAllTeachers: { type: Boolean, default: false },
    toAllStudents: { type: Boolean, default: false },
    toAllExaminationControllers: { type: Boolean, default: false },
    toAll: { type: Boolean, default: false }
  }],
  scheduledAt: { type: Date }, 
  status: {
    type: String,
    enum: ['PENDING', 'SENT', 'FAILED'],
    default: 'PENDING'
  }
}, { timestamps: true });


// /auth
// /initialregister -> firstadmin

// By Admin
//    Create Principal✅
// 		Create department✅
// 	  Create subjects✅

// 		Create hod✅
// 		Add hod to department✅

// 	  Create teacher✅
//    Add teacher to department✅
//    Add teacher to handling subjects✅

//    Create classroom✅
//    assignSubjects✅
//    assignClassTeacher✅

//    Create student✅
//    Add student to classroom✅
//    Create parent and assign parent to student✅


//    Create exam controller✅
//    Create exam controller head✅

// By Exam Controller Head
// 		Create exam✅
// 		Assign timetable✅
// 		Get approval by principal✅

// By Teacher 
// 		Create question paper✅
// 		Get approval by hod✅
		
// By Exam Controller
//    Create exam invigilation✅
// 		Assign invigilators and students✅

// By Exam Controller Head
// 		Assign question papers✅ 
		
// 		Create exam result✅

// By Teacher
// 		Update exam marks✅

// By HOD
// 		Approve exam result✅

// By Students and parents
// 		See results

// By Teacher
//    assignRepresentatives
//    createLeaveRequest

// By Teacher or Hod
//    createLeaveRequest

// By Principal 
//    approveLeaveRequest

// By Student
//    createLeaveRequest
// By Hod
//    approveLeaveRequest

// By Teacher 
//    create attendance record

// By Principal
//    createDisciplinaryActions

//    createNotification

// By HOD
//    createNotification
