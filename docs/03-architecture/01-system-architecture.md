# System Architecture

## 초기 구조

```text
Browser
  |
  v
React + TypeScript
  |
 HTTPS / REST
  |
  v
Spring Boot Modular Monolith
  |
  +-- PostgreSQL + PostGIS
  +-- Object Storage
  +-- Redis (optional)
  +-- AI Service (FastAPI)
  +-- Weather/Map/Taxonomy external services
```

## 원칙

1. 처음부터 microservices로 분리하지 않는다.
2. 도메인 경계를 코드 구조로 먼저 만든다.
3. AI는 독립 서비스로 두어 모델 변경을 쉽게 한다.
4. 이미지/영상은 DB blob이 아니라 object storage 사용.
5. 공간 검색은 PostGIS 사용.
6. 외부 API 의존성은 adapter 계층으로 격리.

## 주요 모듈

- auth
- user
- observation
- media
- taxonomy
- identification
- quality
- project
- mission
- gis
- notification
- research
- admin
