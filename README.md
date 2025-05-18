Risk Evaluator - Documentation Overview
---
## 1.Risk Evaluator

- Risk Evaluator is a Quarkus-based application for transaction risk assessment. 
- The system leverages modern Java, containerization, Keycloak for authentication/authorization, PostgreSQL as the database, and Liquibase for schema migrations. 
- The API is RESTful and documented with OpenAPI. The project is modular, maintainable, and easily deployable with Docker Compose.
---
## Table of Contents
- [Project Structure](#project-structure)
- [Main Components](#main-components)
- [Quick Start](#quick-start)
- [Development & Testing](#development--testing)
- [Database Migrations](#database-migrations)
- [API Documentation](#api-documentation)
- [Authentication](#authentication)
- [Summary](#summary)

---
## Project Structure
```
.
├── docker-compose.yml
├── Dockerfile.jvm
├── docs/
│   └── postman/
│       └── Risk evaluator api.postman_collection.json
├── env/
│   ├── keycloak/
│   │   ├── Dockerfile
│   │   └── realm/
│   │       └── risk-evaluator-realm.json
│   └── postgres/
│       └── conf/
│           └── init_keycloak.sh
├── mvnw
├── mvnw.cmd
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/riskevaluator/
│   │   │       ├── app/       # Application logic and use-cases
│   │   │       ├── domain/    # Domain models and rule engine
│   │   │       └── infra/     # Infra (REST, persistence, caching, logging, security)
│   │   └── resources/
│   │       ├── application.yaml
│   │       ├── banner.txt
│   │       ├── db/changelog/
│   │       │   ├── changes/
│   │       │   │   ├── V1_create_risk_evaluation_log.sql
│   │       │   │   └── V2_create_risk_matched_rules.sql
│   │       │   └── db.changelog-master.yaml
│   │       ├── openapi/
│   │       │   └── BIN-Table-Resource-OpenAPI3.yaml
│   │       └── riskevaluator-sandbox.p12
│   └── test/
│       ├── java/
│       │   └── org/riskevaluator/
│       │       └── ... (unit/integration tests)
│       └── resources/
│           └── application-test.properties
├── target/
└── README.md
```
----

## Main Components
| Path                                         | Description                                                           |
|-----------------------------------------------|-----------------------------------------------------------------------|
| `docker-compose.yml`                         | Defines containers for the app, PostgreSQL, Keycloak, and networking. |
| `Dockerfile.jvm`                             | JVM mode Dockerfile for the Quarkus app.                              |
| `docs/postman/Risk evaluator api.postman_collection.json` | Postman collection to explore/test the API.                           |
| `env/keycloak/realm/risk-evaluator-realm.json`| Keycloak realm with demo users/roles for OIDC.                        |
| `env/postgres/conf/init_keycloak.sh`         | Script for initializing Keycloak database settings.                   |
| `src/main/java/org/riskevaluator/app`        | Business logic and application use-cases.                             |
| `src/main/java/org/riskevaluator/domain`     | Core domain models and risk rules engine.                             |
| `src/main/java/org/riskevaluator/infra`      | REST adapters, persistence, caching, logging, and security layer.     |
| `src/main/resources/application.yaml`        | Main properties/configuration for the application.                    |
| `src/main/resources/db/changelog/`           | Liquibase changelog for DB schema migrations.                         |
| `src/main/resources/openapi/BIN-Table-Resource-OpenAPI3.yaml` | OpenAPI schema for REST API.                                          |
| `src/main/resources/riskevaluator-sandbox.p12`| Sample security key for sandbox MC API.                               |
| `src/test`                                   | Unit and integration tests, with per-test configuration.              |
---
## Quick Start
### Prerequisites
- Java 21
- Maven (or use the included Maven Wrapper)
- Docker & Docker Compose
### Building and Running
1. **Build the application:**
    ```sh
    ./mvnw clean package
    ```
2. **Run application and dependencies ([Quarkus] + [Keycloak] + [Postgres])**
    ```sh
    docker-compose up --build
    ```
    By default, the services will be available:
    - Quarkus app: `http://localhost:8081`
    - Keycloak: `http://localhost:8080`
    - Postgres: `localhost:5433`
3. **Explore REST API documentation:**
    ```
    http://localhost:8080/q/openapi
    ```
4. **Try API requests with Postman:**
    - Use the collection:  
      `docs/postman/Risk evaluator api.postman_collection.json`
---
## Development & Testing
- **Run in developer mode with hot reload:**
    ```sh
    ./mvnw quarkus:dev
    ```
- **Run all tests:**
    ```sh
    ./mvnw test
    ```
- **Developer configuration:**
    - Main app: `src/main/resources/application.yaml`
    - Tests: `src/test/resources/application-test.properties`
---
## Database Migrations
- Database schema changes are managed with **Liquibase**.
- Changelogs and individual migrations can be found under `src/main/resources/db/changelog/`.
    - Main changelog: `db.changelog-master.yaml`
    - SQL scripts: `changes/Vx_*.sql`
- New changesets should be referenced in `db.changelog-master.yaml`.
---
## API Documentation
- **OpenAPI specification:**  
  `src/main/resources/openapi/BIN-Table-Resource-OpenAPI3.yaml`
- **Postman collection:**  
  `docs/postman/Risk evaluator api.postman_collection.json`
---
## Authentication
- **Keycloak** is used for OAuth2/OIDC.
- Sample realm configuration for import:  
  `env/keycloak/realm/risk-evaluator-realm.json`
- The API expects valid OIDC tokens in requests.
---
## **Summary**

- Risk Evaluator provides a complete, container-ready environment for transaction risk management.
- Start it quickly with Docker Compose, explore the API via OpenAPI or Postman, and use Keycloak for secure, standards-based authentication.
---
