# Release Tracker

A REST API for managing software release lifecycle.

## Tech Stack

| Category          | Technology                       |
|-------------------|----------------------------------|
| Language          | Java 21                          |
| Framework         | Spring Boot 4                    |
| Database          | PostgreSQL                       |
| Migrations        | Flyway                           |
| ORM               | Spring Data JPA / Hibernate      |
| Mapping           | MapStruct                        |
| Documentation     | SpringDoc OpenAPI (Swagger UI)   |
| Testing           | JUnit 5, Mockito, Testcontainers |
| Coverage          | JaCoCo                           |
| Code Style        | Spotless / Google Java Format    |
| Build             | Maven                            |
| Containerization  | Docker                           |

## Running the Project

### Prerequisites

- Java 21
- Maven 3.9+
- Docker (for dev/demo environments)

---

### IntelliJ IDEA

1. **Clone the repository**

   ```bash
   git clone <repository-url>
   cd release-tracker
   ```

2. **Start the database**

   ```bash
   docker-compose -f docker-compose.dev.yml up -d
   ```

   This starts a PostgreSQL instance on port `5432` with:
   - Database: `release-tracker`
   - Username: `postgres`
   - Password: `root`

3. **Run the application**

   Open the project in IntelliJ, set the active Spring profile to `dev`, then run `ReleasetrackerApplication`.

   Alternatively, via Maven:

   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
   ```

   The `dev` profile seeds 30 randomised releases on startup using DataFaker.

4. **Access the API**

   - Base URL: `http://localhost:8080/api/v1/releases`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - Health check: `http://localhost:8080/actuator/health`

---

### Docker (Demo Environment)

The demo environment builds the application image and starts both the API and database via Docker Compose. It seeds 30 randomised releases on startup.

1. **Start the demo environment**

   ```bash
   docker-compose -f docker-compose.demo.yml up -d --build
   ```

2. **Access the API**

   - Base URL: `http://localhost:8080/api/v1/releases`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - Health check: `http://localhost:8080/actuator/health`

3. **Stop the environment**

   ```bash
   docker-compose -f docker-compose.demo.yml down -v
   ```

---

## Running Tests

**Unit tests only:**

```bash
./mvnw test
```

**Unit + integration tests with merged coverage report:**

```bash
./mvnw verify
```

The merged coverage report is generated at `target/site/jacoco/index.html`.
