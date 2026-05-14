# OrbitETL — Architecture Decision Record

## Overview
OrbitETL is a generic, configurable ETL platform that allows users to visually design,
configure, and execute data migration workflows across multiple source and target systems.

## Architecture Style
Microservices — each service is independently deployable and has a single responsibility.

## Repository Structure
Monorepo with service-level CI/CD pipelines.
Path-based triggers in Jenkins deploy only the changed service.

## Services
| Service | Responsibility |
|---|---|
| api-gateway | Single entry point, routing, auth |
| config-service | Workflow and connection configuration |
| extract-service | Reads data from source systems |
| transform-service | Applies transformation rules |
| load-service | Writes data to target systems |
| orchestrator-service | Job scheduling and coordination |
| notification-service | Alerts on job success/failure |

## Tech Stack
- Language: Java 17 + Spring Boot 3
- Messaging: Apache Kafka
- Database: PostgreSQL
- Frontend: React + React Flow
- Container: Docker
- Orchestration: Kubernetes on GKE
- Helm: K8s package management
- CI/CD: Jenkins
- Monitoring: Prometheus + Grafana
- Logging: ELK Stack
- Security: Spring Security + JWT

## Key Decisions
1. Monorepo — easier local development, shared schemas, single source of truth
2. Kafka between services — async, decoupled, resilient to service failures
3. Config-driven ETL — workflows stored in DB, not hardcoded
4. React Flow — visual drag-and-drop workflow designer
5. OrbitETL — name reflects modern, cloud-native ETL platform
