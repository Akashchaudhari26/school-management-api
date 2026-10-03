# Architecture Overview

## Application Type

This is a Spring Boot 3.3.3 application written in Java 17. The application uses Maven, Spring Data JPA, PostgreSQL, and Flyway schema migrations.

## Main Application Entry Point

The main entry point is [src/main/java/com/sms/SchoolManagementApplication.java](../src/main/java/com/sms/SchoolManagementApplication.java). It enables Mongo auditing and Spring caching.

## High-Level Architecture

The project is organized as a modular monolith-style Spring application. Business logic is grouped by domain module under [src/main/java/com/sms/modules](../src/main/java/com/sms/modules).

### Layers Observed

- Controllers: expose REST endpoints
- Services: implement business logic
- Repositories: persist and retrieve domain entities through Spring Data JPA
- Domain objects: represent business entities and value objects
- DTOs: carry data between layers
- Mappers: translate between DTOs and entities
- Security filters and providers: handle authentication and authorization

## Module Structure

The main modules observed are:

- IAM: authentication, users, roles, password reset, OTP
- Student: student records, guardians, promotion, file storage
- Staff: staff records, employee codes, assignments
- Attendance: attendance marking, leave management, reports
- Fees: fee structures, payments, receipts
- Exam: marks entry, report cards, grade logic
- Payroll: salary structures and payroll generation
- School Configuration: classes, sections, subjects, academic years
- Audit: audit trail and logging

## Request Handling Pattern

A typical request flows through:

1. Controller receives HTTP request
2. Service implements domain logic
3. Repository interacts with PostgreSQL
4. Response DTO is returned to the client

## Cross-Cutting Concerns

The codebase includes implementation for the following cross-cutting concerns:

- Security via Spring Security and JWT
- Validation through Bean Validation annotations and manual checks
- Exception handling via a centralized exception handler
- Auditing when enabled
- Caching support
- Student files use a configurable filesystem storage provider; production multi-instance deployments should use shared/object storage
