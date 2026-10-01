# ADR-05: Observation MVP and Security Baseline

- Status: Accepted for the first MVP implementation
- Scope: Observation creation, ownership, media, location privacy, and identification records

## Decisions

### 1. MVP boundary

The first usable flow is:

1. An authenticated user creates an observation.
2. The user records observation time and optional description.
3. The user attaches media through a private upload flow.
4. The owner can view, edit, change visibility, and delete their observation.
5. Other users cannot access a private observation or mutate another user's record.

Feed ranking, projects, missions, advanced search, map analytics, and AI inference are outside the first observation CRUD slice.

### 2. Observation and identification are separate

An observation records what was seen, when it was seen, who recorded it, its visibility, and its media.

Taxonomic assertions are stored as separate records. AI predictions must not be represented as confirmed human identifications. Human suggestions, community support, and expert review must retain their source and history.

The first CRUD slice may create observations without a taxon. A single accepted taxon must not be inferred by overwriting identification history.

### 3. Observation visibility and location privacy are independent

Observation visibility controls who can view the observation. Location privacy controls the precision of its coordinates.

Exact coordinates are restricted data. Public responses must use a generalized coordinate or omit the location; they must never serialize the restricted point directly. Sensitive observations default to private location handling.

### 4. Media is private by default

The database stores media metadata and a storage key, not a permanent public URL. Upload handling must validate allowed media types and size, use non-guessable storage keys, and keep uninspected uploads unavailable for display.

The storage implementation must be replaceable so local development storage does not dictate the production provider.

### 5. Migration ownership

Flyway is the source of truth for application schema changes, including required database extensions. Docker initialization scripts must not be the only mechanism needed to prepare a usable application database.

### 6. API response conventions

Successful responses remain compatible with the existing authentication API unless a versioned API change is approved.

The current authentication implementation returns flat errors (`code`, `message`), while the API convention document describes a nested error object. Before adding observation endpoints, update the convention and tests to one agreed format; do not introduce a third format.

## Security baseline

- Every write endpoint requires authentication, CSRF protection where cookie sessions are used, and server-side ownership checks.
- A non-owner must not learn private resource details; use a consistent not-found or forbidden policy and test it.
- Production session cookies must be Secure, HttpOnly, and SameSite=Lax. Local HTTP development may use a separate setting.
- Configure and test a bounded server-side session lifetime.
- Add login and signup abuse protection before public exposure.
- Never allow wildcard CORS with credentialed session requests.
- Do not log passwords, session cookies, CSRF tokens, upload credentials, or exact coordinates.
- Tests must cover authentication bypass, cross-user access, visibility changes, and exact-coordinate non-disclosure.
- Upload tests must cover rejected media types, oversized files, and attempts to access media before inspection succeeds.

## Definition of done for the observation MVP

- Database changes are versioned and reproducible through Flyway.
- Create, list, detail, update, and delete behavior has integration tests.
- Owner and non-owner authorization cases are tested.
- Media remains private until validation and inspection complete.
- Exact coordinates are not returned by public response DTOs.
- API documentation matches the implemented request and error formats.
- `mvn --batch-mode -f apps/api/pom.xml verify` succeeds.