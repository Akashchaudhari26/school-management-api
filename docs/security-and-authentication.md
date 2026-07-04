# Security and Authentication

## Authentication Model

The application uses Spring Security with a stateless JWT-based authentication model.

### Evidence

- [src/main/java/com/sms/config/SecurityConfig.java](../src/main/java/com/sms/config/SecurityConfig.java)
- [src/main/java/com/sms/security/JwtAuthenticationFilter.java](../src/main/java/com/sms/security/JwtAuthenticationFilter.java)
- [src/main/java/com/sms/security/JwtTokenProvider.java](../src/main/java/com/sms/security/JwtTokenProvider.java)

## Security Configuration

The security configuration disables CSRF, uses stateless sessions, and inserts a JWT authentication filter before the username/password authentication filter.

### Observed Behaviors

- Public endpoints are allowed for login and registration-related flows
- Protected endpoints require authentication
- The application uses JWT bearer tokens for request authorization

## Password and Credential Handling

The IAM service handles password hashing and initialization of default users or roles. The implementation uses standard password management logic rather than external identity providers.

## Authorization Notes

The codebase includes role and permission concepts in the IAM model. However, the documentation here is limited to what is explicitly implemented in the code observed.

## Security-Related Components

- JwtAuthenticationFilter
- JwtTokenProvider
- SecurityUtils
- SecurityConfig
- GlobalExceptionHandler for access-related errors
