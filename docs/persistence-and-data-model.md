# Persistence and Data Model

## Persistence Technology

The project uses Spring Data JPA with PostgreSQL. Entities are mapped to relational tables; nested guardian and fee records use relational collection tables, while selected flexible configuration values use PostgreSQL JSONB columns.

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

## Relational Tables

Representative relational tables include:

- `users`, `roles`
- `students`, `student_guardians`, `student_academic_history`
- `staff`, `attendance`, `leave_requests`
- `fees`, `fee_items`, `fee_payments`
- `exam_definitions`, `exam_results`
- `payroll_transactions`, `salary_structures`
- `audit_logs`

## File Storage

Student file metadata is stored in PostgreSQL. The current file provider stores file bytes under a configurable local directory; use shared/object storage for multi-instance production deployments.

## Notes on Data Model Completeness

The repository contains many domain classes and DTOs. The documentation here is limited to the classes that are visible in the source tree and directly referenced by the application logic.
