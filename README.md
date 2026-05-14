# OrbitETL

A modern, cloud-native ETL platform with a visual drag-and-drop workflow designer.

## What is OrbitETL?
OrbitETL allows users to visually design, configure, and execute data migration
workflows across multiple source and target systems — databases, files, and REST APIs.

## Architecture
Microservices built with Java 17 + Spring Boot 3, deployed on Kubernetes (GKE),
with Apache Kafka for async messaging between services.

## Services
| Service | Port | Responsibility |
|---|---|---|
| api-gateway | 8080 | Routing + Auth |
| config-service | 8081 | Workflow configuration |
| extract-service | 8082 | Data extraction |
| transform-service | 8083 | Data transformation |
| load-service | 8084 | Data loading |
| orchestrator-service | 8085 | Job scheduling |
| notification-service | 8086 | Alerts + notifications |

## Tech Stack
- **Backend:** Java 17, Spring Boot 3, Apache Kafka, PostgreSQL
- **Frontend:** React, React Flow
- **Infrastructure:** Docker, Kubernetes, Helm, GCP
- **CI/CD:** Jenkins
- **Monitoring:** Prometheus, Grafana
- **Logging:** ELK Stack

## Getting Started
Coming soon.
