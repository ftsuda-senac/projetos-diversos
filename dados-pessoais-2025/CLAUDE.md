# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Reference Spring Boot 3.x + Java 21 project demonstrating REST API + Thymeleaf MVC patterns for personal data management (Pessoas, Interesses, Fotos).

## Commands

```bash
# Run (dev profile active by default)
./mvnw spring-boot:run

# Run with explicit profile
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Build JAR
./mvnw clean package

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=PessoaServiceTest

# Code coverage report (output: target/site/jacoco/)
./mvnw verify
```

After starting, access:
- App: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:dadopessoais2025bd`)

## Architecture

### Dual Interface Pattern
The app exposes the same domain via two interfaces:
- **REST API** (`rest/` package): JSON responses, pagination via `PagedContentContainerDto`/`ListContentContainerDto` wrappers
- **Thymeleaf MVC** (`webmvc/` package): Form-based UI with `AlertMessage` flash messages

### Domain Layer (`dominio/`)
- **Entities** use a dual-ID strategy: internal sequential `id` (never exposed to clients) + `publicId` (UUID, used in all REST URLs)
- **Generic `CrudService<T,TC,TU,ID>`** interface defines CRUD contract; `PessoaService` implements it using Spring Data JPA + password encoding
- **DTO separation by operation**: `PessoaInclusaoDto` (create), `PessoaAlteracaoDto` (update), `PessoaMvcDto` (JavaBean for forms), `PessoaDto` (API response with nested fotos)
- **Custom validators**: `@UsernameUnico` (database uniqueness check), `@SenhasIguais` (cross-field password confirmation)

### File Uploads
- Photos stored in filesystem at path configured by `app.upload.path` (default `C:/uploads/` — override for non-Windows)
- URL mapping via `UrlMapper` at pattern `/uploads/{pessoaId}/fotos/{filename}`
- `WebMvcConfig` registers the upload directory as a static resource handler

### Security
`SecurityConfig` is configured with `STATELESS` sessions and CSRF disabled. JWT authentication is **not yet implemented** — all endpoints are currently open. `DelegatingPasswordEncoder` defaults to bcrypt but also accepts `{noop}` prefixed passwords for migration.

## Configuration

**Environment variables** are loaded from a `.env` file (dev profile only, via spring-dotenv). See `.env.example` for required variables.

Key `application.properties` settings to know:
- `app.upload.path` — filesystem path for photo storage (must exist and be writable)
- `app.upload.url-prefix` — URL prefix for serving photos
- Pagination: 1-indexed pages, default page size 20 (`spring.data.web.pageable.*`)
- H2 file-mode DB (persistent) is commented out; uncomment `spring.datasource.url=jdbc:h2:file:~/dadopessoais2025bd` to enable

## Testing Approach

Tests use a separate H2 in-memory database (`src/test/resources/application.properties`).

Test types present:
- **JPA Repository tests** — `@DataJpaTest`
- **Service unit tests** — Mockito mocks
- **REST controller tests** — `@WebMvcTest` with MockMvc
- **Integration tests** — RestAssured against full context

Mockito agent is configured in `maven-surefire-plugin` for Java 21 compatibility (no `--add-opens` needed).
