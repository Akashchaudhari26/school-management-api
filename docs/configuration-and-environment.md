# Configuration and Environment

## Runtime Configuration

The runtime configuration is defined in [src/main/resources/application.yml](../src/main/resources/application.yml).

### Observed Configuration Areas

- MongoDB connection settings
- Server port
- JWT settings
- Audit enablement toggle
- Other application-level properties

## Build and Runtime Tools

The project uses:

- Maven
- Java 17
- Spring Boot 3.3.3
- Lombok
- Spring Data MongoDB
- Validation starter
- Mail starter
- OpenAPI/Swagger support
- PDF generation libraries

## Environment Considerations

For local development, the application expects the MongoDB configuration to be available through the application configuration. This includes any Atlas or hosted MongoDB connection string.

## Deployment Notes

The source does not show containerization artifacts or deployment manifests in the current repository snapshot. Any deployment configuration would need to be added separately.
