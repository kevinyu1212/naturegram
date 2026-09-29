# REST API Specification

Base:
`/api/v1`

## Auth

```http
GET  /auth/csrf
POST /auth/signup
POST /auth/login
POST /auth/logout
GET  /auth/me
```

Signup accepts `username`, `email`, and `password`. Login accepts `usernameOrEmail` and `password`. Signup returns `201`, login returns the authenticated user, and logout returns `204`.

Before signup, login, or logout, call `GET /auth/csrf` and send its `token` as `X-XSRF-TOKEN` with the `XSRF-TOKEN` cookie. Login rotates the CSRF token; fetch a new one before the next write request. The session cookie is HttpOnly and SameSite=Lax.

Unauthenticated protected requests return `401`. Errors use `{ "code": "...", "message": "..." }`.

## Observations

```http
POST   /observations
GET    /observations
GET    /observations/{id}
PATCH  /observations/{id}
DELETE /observations/{id}
```

Query:
- taxonId
- bbox
- observedFrom
- observedTo
- userId
- projectId
- quality
- page/cursor
- limit

## Identifications

```http
POST   /observations/{id}/identifications
GET    /observations/{id}/identifications
DELETE /identifications/{id}
```

## Taxa

```http
GET /taxa
GET /taxa/{id}
GET /taxa/{id}/observations
GET /taxa/{id}/distribution
```

## Projects

```http
POST /projects
GET  /projects
GET  /projects/{id}
PATCH /projects/{id}
POST /projects/{id}/join
POST /projects/{id}/leave
```

## Media

```http
POST /media/presign
POST /media/complete
DELETE /media/{id}
```

## Research

```http
GET /research/datasets
POST /research/datasets
POST /research/datasets/{id}/export
```

## Admin

```http
GET  /admin/reports
POST /admin/reports/{id}/resolve
GET  /admin/audit-logs
```

## API 규칙

- JSON
- UTC 저장, 사용자 로컬 timezone 표시
- cursor pagination 우선
- 표준 error object
- idempotency가 필요한 write API에는 Idempotency-Key 고려
