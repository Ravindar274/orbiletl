# OrbitETL — Architecture Decision Record

## Overview
OrbitETL is a modern, cloud-native, generic ETL platform that allows users to visually
design, configure, and execute data migration workflows across multiple source and target
systems — databases, files, and REST APIs. Think Talend or Azure Data Factory, but
open source and built on modern cloud-native stack.

## Repository Structure
**Monorepo** — all services in one repository with service-level CI/CD pipelines.
Path-based triggers in Jenkins deploy only the changed service.

```
orbitetl/
├── services/
│   ├── workflow-service/       # manages workflows, connections, transformation rules
│   ├── extract-service/        # reads data from source systems
│   ├── transform-service/      # applies transformation rules
│   ├── load-service/           # writes data to target systems
│   ├── orchestrator-service/   # job scheduling and coordination
│   ├── notification-service/   # alerts on job success/failure
│   └── api-gateway/            # single entry point, routing, auth
├── frontend/
│   └── orbitetl-ui/            # React + React Flow visual workflow designer
├── infrastructure/
│   ├── helm/                   # Helm charts for all services
│   ├── kubernetes/             # Raw K8s manifests
│   └── jenkins/                # Jenkinsfiles
├── docker/
│   └── docker-compose.yml      # Local development
└── docs/
    └── architecture.md         # This file
```

---

## Services

### 1. workflow-service (Port 8081)
**Responsibility:** Manages all OrbitETL business configuration.
- Workflow definitions (source + target + ordered transformation rules)
- Connection configurations (source and target systems)
- Transformation rule definitions

**Base package:** `com.orbitetl.workflow`

**Package structure (same pattern for all services):**
```
com.orbitetl.workflow
├── controller        # REST API endpoints
├── service           # Business logic (interface + impl pattern)
├── repository        # JPA repositories
├── entity            # JPA entity classes
├── dto               # Request and Response DTOs
├── mapper            # MapStruct mappers
├── exception         # Custom exceptions + GlobalExceptionHandler
├── config            # Spring configuration classes
└── constant          # Constants classes (e.g. ConnectionTypes)
```

**Database tables:**
```
connection
----------
id                  UUID PK (auto generated)
name                VARCHAR(255) NOT NULL
type                VARCHAR(50) NOT NULL     -- use ConnectionTypes constants
host                VARCHAR(255)
port                INTEGER
database_name       VARCHAR(255)
username            VARCHAR(255)
password            VARCHAR(500)             -- stored encrypted (future)
file_path           VARCHAR(500)             -- for CSV sources
api_url             VARCHAR(500)             -- for REST API sources
created_at          TIMESTAMP (auto, immutable)
updated_at          TIMESTAMP (auto)


transformation_rule
-------------------
id                  UUID PK
name                VARCHAR(255) NOT NULL
description         VARCHAR(500)
type                VARCHAR(50) NOT NULL     -- FILTER, MAP, SORT, JOIN, MERGE, VALIDATE
configuration       JSONB                    -- rule-specific config, flexible per type
created_at          TIMESTAMP (auto, immutable)
updated_at          TIMESTAMP (auto)


workflow
--------
id                  UUID PK
name                VARCHAR(255) NOT NULL
description         VARCHAR(500)
source_connection_id    UUID FK → connection
target_connection_id    UUID FK → connection
status              VARCHAR(50)              -- DRAFT, ACTIVE, INACTIVE
created_at          TIMESTAMP (auto, immutable)
updated_at          TIMESTAMP (auto)


workflow_transformation_rule     (junction table — many to many)
----------------------------
id                      UUID PK
workflow_id             UUID FK → workflow
transformation_rule_id  UUID FK → transformation_rule
execution_order         INTEGER              -- order rules are applied


job_execution
-------------
id                  UUID PK
workflow_id         UUID FK → workflow
status              VARCHAR(50)              -- RUNNING, SUCCESS, FAILED, CANCELLED
started_at          TIMESTAMP
completed_at        TIMESTAMP
rows_extracted      BIGINT
rows_transformed    BIGINT
rows_loaded         BIGINT
error_message       TEXT                     -- populated on failure
triggered_by        VARCHAR(100)             -- MANUAL, SCHEDULER
created_at          TIMESTAMP (auto, immutable)
-- NO updated_at: job execution is immutable, like a receipt
```

### 2. extract-service (Port 8082)
**Responsibility:** Reads data from source systems and publishes to Kafka.
- Connects to MySQL, PostgreSQL, CSV, REST API sources
- Fetches workflow config from workflow-service
- Publishes raw extracted data to Kafka topic `raw-data`

### 3. transform-service (Port 8083)
**Responsibility:** Consumes raw data from Kafka, applies transformation rules.
- Consumes from Kafka topic `raw-data`
- Fetches transformation rules from workflow-service
- Applies rules in execution_order sequence
- Publishes transformed data to Kafka topic `transformed-data`

### 4. load-service (Port 8084)
**Responsibility:** Consumes transformed data from Kafka, writes to target systems.
- Consumes from Kafka topic `transformed-data`
- Connects to target systems (MySQL, PostgreSQL, CSV, REST API)
- Reports rows loaded back to workflow-service job_execution

### 5. orchestrator-service (Port 8085)
**Responsibility:** Job scheduling and coordination.
- Triggers ETL jobs (manual or scheduled)
- Coordinates extract → transform → load sequence via Kafka
- Updates job_execution status throughout the pipeline

### 6. notification-service (Port 8086)
**Responsibility:** Alerts on job success/failure.
- Consumes job status events from Kafka
- Sends email or Slack notifications

### 7. api-gateway (Port 8080)
**Responsibility:** Single entry point for all client requests.
- Routes requests to correct service
- Handles authentication and authorization
- Spring Cloud Gateway

---

## Frontend
**orbitetl-ui** — React + React Flow visual workflow designer.

Screens:
- **Workflows** — drag and drop canvas to build pipelines (React Flow)
- **Connections** — manage source and target connection configs
- **Rules** — define reusable transformation rules (called Components in UI)
- **Jobs** — view running and historical job executions
- **Observe** — embedded Grafana/Kibana dashboards

---

## Tech Stack

| Layer | Technology | Why |
|---|---|---|
| Language | Java 17 + Spring Boot 3.5.14 | LTS, enterprise standard |
| Messaging | Apache Kafka | Async, decoupled, resilient |
| Database | PostgreSQL 15 | JSONB support, enterprise grade |
| ORM | Spring Data JPA + Hibernate | Industry standard |
| Mapping | MapStruct | Compile-time, no reflection overhead |
| Boilerplate | Lombok | Reduces boilerplate, compile-time |
| Frontend | React + React Flow | Visual drag-and-drop designer |
| Container | Docker | Environment parity |
| Orchestration | Kubernetes on GKE | Enterprise grade, cloud native |
| Helm | K8s package manager | Repeatable deployments |
| CI/CD | Jenkins | Industry standard, path-based triggers |
| Monitoring | Prometheus + Grafana | Metrics and dashboards |
| Logging | ELK Stack | Centralized logs across all services |
| Security | Spring Security + JWT | Industry standard auth |
| API Gateway | Spring Cloud Gateway | Single entry point |

---

## Key Architecture Decisions

### 1. Monorepo over Multirepo
Path-based Jenkins triggers give deployment independence without multirepo overhead.
Easier local development, shared schemas, single source of truth.

### 2. Kafka between services
Async communication — extract, transform, load services are fully decoupled.
If transform-service is down, extract-service keeps publishing. No data loss.

### 3. Config-driven ETL
Workflows stored in database, not hardcoded. Generic platform, not one-time pipeline.

### 4. JSONB for transformation rule configuration
Different rule types (FILTER, MAP, SORT) have different config shapes.
JSONB handles polymorphic config without separate tables or nullable columns.
Adding new rule types requires zero database changes.

### 5. String over Enum for extensible types
ConnectionType, RuleType stored as VARCHAR with constant classes.
Adding new types doesn't require recompiling shared code across services.

### 6. UUID over auto-increment IDs
Globally unique across all services and databases.
Generated in application code without database round trip.
Essential in distributed microservices architecture.

### 7. Interface-based services
Program to abstractions — Dependency Inversion (SOLID).
Easier unit testing with mocks, easier to swap implementations.

### 8. DTO pattern
Never expose entities directly from REST API.
Protects sensitive fields (password never in response).
Decouples API contract from database schema.

### 9. Two config service distinction
Spring Cloud Config Server — manages application.properties across services (to be added)
workflow-service — manages OrbitETL business configuration (workflows, connections, rules)

---

## API Conventions

### URL Pattern
```
/api/v1/{resource}          GET all, POST create
/api/v1/{resource}/{id}     GET one, PUT update, DELETE
/api/v1/{resource}?type=X   GET filtered
```

### HTTP Status Codes
```
201 CREATED      — resource created
200 OK           — resource returned
204 NO CONTENT   — delete successful
400 BAD REQUEST  — validation failed
404 NOT FOUND    — resource not found
409 CONFLICT     — duplicate resource
500 SERVER ERROR — unexpected error
```

### Error Response Format
```json
{
    "timestamp": "2026-05-16T10:54:38.991",
    "status": 409,
    "error": "Conflict",
    "message": "Connection with name 'X' already exists"
}
```

---

## Local Development Setup

### Prerequisites
- Java 21 (Eclipse Temurin)
- Maven 3.9+
- Docker Desktop
- Node 22+
- IntelliJ IDEA (with Lombok plugin)

### Start local database
```bash
cd docker
docker-compose up -d
```

### Run a service
```bash
cd services/workflow-service
mvn spring-boot:run
```

### Verify health
```
http://localhost:8081/actuator/health
```

---

## Commit Convention
```
feat(service-name): description     — new feature
fix(service-name): description      — bug fix
refactor(service-name): description — restructure
chore: description                  — setup, config
docs: description                   — documentation
test(service-name): description     — tests
```

---

## Progress Tracker

### workflow-service
- [x] Project scaffolded
- [x] JPA entities (Connection, TransformationRule, Workflow, WorkflowTransformationRule, JobExecution)
- [x] Connection API (CRUD)
- [x] TransformationRule API (CRUD)
- [ ] Workflow API (CRUD)
- [ ] JobExecution API
- [ ] Dockerize
- [ ] Helm chart

### extract-service
- [ ] Scaffold
- [ ] Kafka producer
- [ ] MySQL extractor
- [ ] PostgreSQL extractor
- [ ] CSV extractor

### transform-service
- [ ] Scaffold
- [ ] Kafka consumer/producer
- [ ] Rule engine

### load-service
- [ ] Scaffold
- [ ] Kafka consumer
- [ ] Database loader

### orchestrator-service
- [ ] Scaffold
- [ ] Job trigger API
- [ ] Kafka coordination

### notification-service
- [ ] Scaffold
- [ ] Email notifications

### api-gateway
- [ ] Scaffold
- [ ] Routing config
- [ ] JWT auth

### Frontend
- [ ] React project setup
- [ ] React Flow canvas
- [ ] Connection management UI
- [ ] Workflow designer UI
- [ ] Job monitoring UI

### Infrastructure
- [x] Docker compose (local)
- [ ] Helm charts
- [ ] GKE deployment
- [ ] Jenkins CI/CD
- [ ] Prometheus + Grafana
- [ ] ELK Stack

### workflow-service
- [x] Project scaffolded
- [x] JPA entities (Connection, TransformationRule, Workflow, WorkflowTransformationRule, JobExecution)
- [x] Connection API (CRUD)
- [x] TransformationRule API (CRUD)
- [x] Workflow API (CRUD)
- [x] JobExecution API (read-only)
- [ ] Dockerize
- [ ] Helm chart