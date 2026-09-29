# Technology Stack Decision

## Recommended

| 영역 | 선택 | 이유 |
|---|---|---|
| Frontend | React + TypeScript + Vite | 생산성/생태계 |
| UI | CSS Modules 또는 Tailwind | 팀 규칙에 따라 선택 |
| Backend | Java + Spring Boot | 안정적 REST/JPA/Security |
| ORM | Spring Data JPA | MVP 생산성 |
| DB | PostgreSQL | 데이터/검색 확장성 |
| Spatial | PostGIS | 생물다양성 GIS |
| Cache | Redis | 추후 도입 |
| AI | Python + FastAPI | ML 생태계 |
| Storage | S3-compatible | 미디어 분리 |
| Map | MapLibre GL JS | vendor lock-in 감소 |
| Auth | Spring Security | 서버 권한 통제 |
| API Spec | OpenAPI | 계약 중심 개발 |
| CI | GitHub Actions | 저장소 연계 |
| Container | Docker | 환경 일관성 |

## 선택하지 않은 것

초기에는 Kubernetes, Kafka, Elasticsearch, microservices를 필수로 도입하지 않는다. 데이터 규모와 병목이 확인된 뒤 도입한다.
