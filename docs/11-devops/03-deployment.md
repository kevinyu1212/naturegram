# Deployment

## Initial

```text
Cloudflare/DNS
    |
Frontend Hosting
    |
Backend Server
    |
PostgreSQL/PostGIS
    |
Object Storage

AI Server
    ^
Backend
```

## HTTPS
모든 production traffic은 HTTPS.

## Backup
- DB daily backup
- retention 정책
- restore test
- object storage versioning 검토

## Monitoring
- uptime
- API latency
- error rate
- DB connection
- storage
- AI inference failure
- queue backlog

## Incident
- severity 정의
- 담당자
- 대응 시간
- 원인 분석
- 재발 방지
