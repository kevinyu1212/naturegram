# Naturegram

> AI × 시민과학 × 생물다양성 데이터 플랫폼

Naturegram은 누구나 자연을 관찰하고, AI의 도움을 받아 생물을 식별하고, 커뮤니티와 전문가의 검증을 거쳐, 축적된 관찰 데이터를 연구·교육·보전 활동에 활용할 수 있도록 설계하는 시민과학 플랫폼이다.

## 핵심 루프

관찰 → AI 보조 식별 → 커뮤니티/전문가 검증 → 데이터 품질 평가 → 생물다양성 데이터 → 연구/교육/보전 → 새로운 관찰

## 문서 우선 원칙

이 저장소는 코드보다 먼저 제품의 문제, 요구사항, 데이터 모델, API 계약, 품질 기준, 보안, 운영 정책을 명확히 하는 것을 목표로 한다.

## 문서 구조

- `docs/00-product/` 제품 비전·PRD·범위
- `docs/01-requirements/` 기능/비기능 요구사항
- `docs/02-ux/` 사용자 흐름·정보구조·화면 명세
- `docs/03-architecture/` 시스템 아키텍처·ADR
- `docs/04-data/` ERD·데이터 사전·생물분류·품질
- `docs/05-api/` REST API 설계
- `docs/06-ai/` AI 식별/추론 설계
- `docs/07-gis/` GIS·위치정보 설계
- `docs/08-security/` 보안·개인정보
- `docs/09-governance/` 데이터 라이선스·거버넌스
- `docs/10-testing/` 테스트 전략
- `docs/11-devops/` 개발환경·CI/CD·배포
- `docs/12-roadmap/` 개발 로드맵·백로그
- `docs/13-research/` 경쟁/연구 참고자료

## 초기 기술 방향

- Frontend: React + TypeScript
- Backend: Spring Boot + Java
- Database: PostgreSQL + PostGIS
- Object Storage: S3-compatible storage
- Cache: Redis (필요 시)
- AI Service: Python + FastAPI
- Map: MapLibre GL JS
- API: REST + OpenAPI
- Auth: Spring Security + JWT/OAuth2
- DevOps: Docker + GitHub Actions

초기에는 모놀리식 백엔드로 시작하고, 실제 병목이 확인될 때만 서비스를 분리한다.

## 현재 저장소 상태

GitHub 저장소는 초기 상태(빈 저장소)이며 이 문서 세트가 첫 번째 기준선이다.
