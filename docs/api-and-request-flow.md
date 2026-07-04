# API and Request Flow

## Controller Structure

The application exposes REST controllers for each major business domain. The controller layer is responsible for parsing incoming HTTP requests and delegating to services.

## Example Flow: Authentication

1. Client calls an authentication endpoint in the IAM module
2. The controller delegates to AuthService
3. The service validates credentials or triggers registration logic
4. A JWT token is issued or authentication state is returned

## Example Flow: Student Creation

1. StudentController receives the request
2. StudentServiceImpl executes creation logic
3. StudentRepository persists the entity
4. A DTO response is returned

## Example Flow: Fee Payment

1. FeeController processes a payment request
2. FeeServiceImpl updates the fee ledger
3. A receipt PDF may be generated through FeeReceiptPdfService
4. The updated fee status is returned to the caller

## Observed Endpoint Categories

- Auth endpoints
- User management endpoints
- Student CRUD and search endpoints
- Staff CRUD and search endpoints
- Attendance and leave endpoints
- Fee and payment endpoints
- Exam and report endpoints
- Payroll endpoints
- School configuration endpoints
- Audit endpoints

## Notes on API Completeness

The repository contains controller classes and request/response DTOs for many flows. Some flows may be simplified or partially implemented, but the endpoint surface is clearly present in the source.
