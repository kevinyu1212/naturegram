# Development Environment

## Recommended

- Windows 11
- Git
- GitHub
- VS Code
- Java 21 LTS
- Node.js LTS
- Python 3.11+
- Docker Desktop
- PostgreSQL 16+
- PostGIS
- DBeaver 또는 pgAdmin

## Local services

```text
Frontend: 5173
Backend: 8080
AI: 8000
PostgreSQL: 5432
Redis: 6379
```

## Environment

```text
.env
.env.example
```

비밀값은 `.env`에만 둔다.

## Local database and cache

PowerShell에서 `.env.example`을 복사하고 `POSTGRES_PASSWORD`를 로컬 전용 값으로 설정한 뒤 서비스를 시작한다.

```powershell
Copy-Item .env.example .env
code .env
docker compose --env-file .env -f .\infra\docker\compose.yaml up -d
docker compose --env-file .env -f .\infra\docker\compose.yaml ps
```

서비스 중지:

```powershell
docker compose --env-file .env -f .\infra\docker\compose.yaml down
```

데이터 볼륨은 `down` 후에도 유지된다. 로컬 데이터까지 삭제하려면 `down -v`를 사용한다.

## Git

권장 브랜치:
- main
- develop
- feature/*
- fix/*
- docs/*

커밋 예:
- feat:
- fix:
- docs:
- refactor:
- test:
- chore:
