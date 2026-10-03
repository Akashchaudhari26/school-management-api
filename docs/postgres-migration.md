# PostgreSQL Migration

## Status

The persistence code has been converted to Spring Data JPA, with PostgreSQL as the only configured database. The initial Flyway schema is added; the remaining work is to validate it against a live PostgreSQL instance and verify API workflows.

## Foundation added

- PostgreSQL JDBC, Spring Data JPA, and Flyway dependencies are available.
- A PostgreSQL profile reads its connection settings from environment variables and uses Hibernate schema validation.
- JWT and database connection settings are externalized. Supply `JWT_SECRET`, `DATABASE_URL`, `DATABASE_USERNAME`, and `DATABASE_PASSWORD` through the runtime environment.
- Student file storage now uses a configurable filesystem provider in place of GridFS. For a multi-instance production deployment, use shared/object storage rather than a node-local disk.

## Migration order

1. Apply the versioned initial Flyway schema and verify that it matches all JPA entities.
2. Review relational constraints and tenant-scoped unique keys before enabling school data entry.
3. Verify student, staff, attendance, fee, exam, payroll, setup, authentication, and audit workflows against PostgreSQL.
4. Choose shared/object storage for student documents before deploying multiple application instances.

Keep controller routes and DTOs stable during the persistence migration so the Angular client does not need a simultaneous rewrite. The project currently has no production data to transfer, so the migration does not include a Mongo-to-PostgreSQL data loader.

## Local PostgreSQL profile

For a persistent local PostgreSQL database on Windows, install Docker Desktop and start from the backend repository root:

1. Copy `.env.example` to `.env` and replace the sample database password.
2. Run `docker compose --env-file .env up -d`.
3. The Compose bind mount stores database files under `C:\SchoolManagement\postgres-data` by default. This folder remains on the host when the container is stopped, updated, or recreated. To store it elsewhere on C:, change `POSTGRES_DATA_DIR` in `.env`.
4. Set the Spring application's environment in the PowerShell session where it will run:

   ```powershell
   $env:DATABASE_URL = "jdbc:postgresql://localhost:5432/schooldb"
   $env:DATABASE_USERNAME = "schooldb"
   $env:DATABASE_PASSWORD = "your-password-from-.env"
   $env:JWT_SECRET = "your-long-random-secret"
   .\mvnw.cmd spring-boot:run
   ```

   If you changed `DATABASE_NAME`, `DATABASE_PORT`, or credentials in `.env`, update `DATABASE_URL` and the other PowerShell values to match. Spring Boot does not automatically read Docker Compose's `.env` file.

Use `docker compose --env-file .env ps` to check PostgreSQL health and `docker compose --env-file .env logs -f postgres` to inspect its startup logs. Stop it with `docker compose --env-file .env down`; this keeps the C: data directory. **Do not use `docker compose down -v`** if you want to preserve database data.

The local C: directory is persistent storage, not a backup. Back it up to a separate drive or secure remote location before using real school records. For production or multiple application instances, use managed PostgreSQL and shared/object storage rather than depending on one PC's disk.

Production secrets must be supplied outside source control. Rotate the previously committed MongoDB password and JWT signing key; removing them from the current config does not remove them from Git history.
