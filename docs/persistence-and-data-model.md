# Persistence and Data Model

## Persistence Technology

The project uses Spring Data MongoDB. Entities are mapped with MongoDB document annotations and stored in collections.

## Domain Model Observations

The application contains domain classes for:

- User and Role
- Student and GuardianRef
- Staff
- Attendance records and leave requests
- Fees, fee items, and fee payments
- Exams and marks
- Payroll entities
- School classes, sections, and subjects
- Audit logs

## Collection-Oriented Design

Several entities are annotated as MongoDB documents, such as:

- User
- Student
- Staff
- Subject
- AuditLog
- LeaveRequest

## File Storage

Student-related files are handled through MongoDB GridFS via GridFsFileStorageService.

## Notes on Data Model Completeness

The repository contains many domain classes and DTOs. The documentation here is limited to the classes that are visible in the source tree and directly referenced by the application logic.
