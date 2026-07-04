# School Management System Documentation

This document set was created from source inspection of the Spring Boot application in this repository. It reflects the implementation that exists in the codebase as of the current inspection.

## Project Scope

This repository contains a modular school management system implemented in Java with Spring Boot and MongoDB. The application includes modules for identity and access management, student and staff management, attendance, fees, exams, payroll, school configuration, and audit logging.

## Key Evidence

The documentation below is based on the following source files:

- [pom.xml](../pom.xml)
- [src/main/resources/application.yml](../src/main/resources/application.yml)
- [src/main/java/com/sms/SchoolManagementApplication.java](../src/main/java/com/sms/SchoolManagementApplication.java)
- [src/main/java/com/sms/config/SecurityConfig.java](../src/main/java/com/sms/config/SecurityConfig.java)
- The controller, service, repository, domain, and DTO classes under [src/main/java/com/sms/modules](../src/main/java/com/sms/modules)

## Documentation Index

- [Architecture Overview](./architecture-overview.md)
- [Module Inventory](./module-inventory.md)
- [Security and Authentication](./security-and-authentication.md)
- [API and Request Flow](./api-and-request-flow.md)
- [Configuration and Environment](./configuration-and-environment.md)
- [Persistence and Data Model](./persistence-and-data-model.md)
- [Validation, Exceptions, and Error Handling](./validation-exceptions-and-error-handling.md)
- [Operational Notes and Gaps](./operational-notes-and-gaps.md)
