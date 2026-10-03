# Configuration and Environment

## Runtime Configuration

The runtime configuration is defined in [src/main/resources/application.yml](../src/main/resources/application.yml).

### Observed Configuration Areas

- PostgreSQL connection settings
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
- Spring Data JPA
- PostgreSQL JDBC
- Flyway
- Validation starter
- Mail starter
- OpenAPI/Swagger support
- PDF generation libraries

## Environment Considerations

The `postgres` Spring profile is the default. Set `DATABASE_URL`, `DATABASE_USERNAME`, and `DATABASE_PASSWORD` for PostgreSQL, and set `JWT_SECRET` for token signing. The schema is managed by Flyway and Hibernate validates the mapped tables on startup.

## Deployment Notes

The source does not show containerization artifacts or deployment manifests in the current repository snapshot. Any deployment configuration would need to be added separately.
