# Module Inventory

## 1. IAM Module

Location: [src/main/java/com/sms/modules/iam](../src/main/java/com/sms/modules/iam)

### Responsibilities

- User authentication and registration
- JWT-based login flow
- Password reset and OTP operations
- Role and permission handling

### Key Classes

- AuthController
- UserController
- AuthService
- PasswordResetService
- OtpService
- User, Role

## 2. Student Module

Location: [src/main/java/com/sms/modules/student](../src/main/java/com/sms/modules/student)

### Responsibilities

- Student record management
- Guardian linkage
- Student search and promotion
- Student file storage through GridFS

### Key Classes

- StudentController
- StudentServiceImpl
- StudentRepository
- Student
- StudentMapper
- GridFsFileStorageService

## 3. Staff Module

Location: [src/main/java/com/sms/modules/staff](../src/main/java/com/sms/modules/staff)

### Responsibilities

- Staff profile management
- Employee code generation
- Subject and class assignment
- Staff-to-IAM linkage

### Key Classes

- StaffController
- StaffServiceImpl
- StaffRepository
- Staff
- StaffMapper

## 4. Attendance Module

Location: [src/main/java/com/sms/modules/attendance](../src/main/java/com/sms/modules/attendance)

### Responsibilities

- Mark attendance
- View class and student attendance
- Leave request and approval flows
- Reporting endpoints

### Key Classes

- AttendanceController
- LeaveController
- ReportController
- AttendanceServiceImpl
- LeaveRequest
- AttendanceRecord

## 5. Fees Module

Location: [src/main/java/com/sms/modules/fees](../src/main/java/com/sms/modules/fees)

### Responsibilities

- Fee master definition
- Fee creation per student
- Payment processing
- Receipt generation
- Payment history

### Key Classes

- FeeController
- FeeMasterController
- FeeServiceImpl
- FeeReceiptPdfService
- Fee, FeePayment, FeeItem

## 6. Exam Module

Location: [src/main/java/com/sms/modules/exam](../src/main/java/com/sms/modules/exam)

### Responsibilities

- Exam definitions
- Marks submission
- Mark sheets and report cards
- Grade computation

### Key Classes

- ExamController
- ExamServiceImpl
- Exam, Mark, ReportCard-related classes

## 7. Payroll Module

Location: [src/main/java/com/sms/modules/payroll](../src/main/java/com/sms/modules/payroll)

### Responsibilities

- Salary structure management
- Payroll generation
- Payslip access
- Ledger review

### Key Classes

- PayrollController
- PayrollServiceImpl

## 8. School Configuration Module

Location: [src/main/java/com/sms/modules/schoolConfig](../src/main/java/com/sms/modules/schoolConfig)

### Responsibilities

- Academic years
- Classes and sections
- Subjects
- Subject/class assignment

### Key Classes

- SchoolConfigController
- SchoolConfigService
- SchoolClass, Section, Subject

## 9. Audit Module

Location: [src/main/java/com/sms/modules/audit](../src/main/java/com/sms/modules/audit)

### Responsibilities

- Collect audit logs for business actions
- Expose audit history through controller endpoints

### Key Classes

- AuditAspect
- AuditController
- AuditLog
