# Observation MVP API Contract

## Scope

This contract documents the first authenticated observation API slice. Public browsing, media upload, location responses, taxon search, projects, and cursor pagination are not implemented in this slice.

## Authentication and CSRF

All observation routes require the existing session authentication. Cookie-authenticated write requests must include the current CSRF cookie and matching `X-XSRF-TOKEN` header. Fetch a fresh CSRF token after login as described in the authentication API contract.

Errors use the existing JSON shape: `code` and `message`.

## Routes

### Create an observation

`POST /api/v1/observations`

Required JSON field:
- `observedAt`: ISO-8601 timestamp with offset or UTC marker

Optional fields:
- `title`: string, at most 200 characters, or null
- `description`: string, at most 5000 characters, or null
- `taxonId`: UUID or null; must refer to an existing taxon
- `visibility`: `public`, `limited`, or `private`; defaults to `private`

The owner is always taken from the authenticated session, never from a request-supplied owner ID. Success returns `201 Created`.

### List the caller's observations

`GET /api/v1/observations?limit=20&offset=0`

- Returns only observations owned by the authenticated caller.
- `limit` must be from 1 to 100.
- `offset` must be zero or greater.
- Results are ordered by creation time descending, then ID descending.
- `userId`, `projectId`, `bbox`, quality, and date filters are not supported yet.

### Read one observation

`GET /api/v1/observations/{id}`

Only the owner can read the record in this MVP slice. A missing record and a record owned by another user both return `404 Not Found`.

### Patch an observation

`PATCH /api/v1/observations/{id}`

Only the owner can update it. Supported fields are `observedAt`, `title`, `description`, `taxonId`, and `visibility`. Unknown fields are rejected. Explicit null clears `title`, `description`, or `taxonId`; `observedAt` and `visibility` cannot be null. An empty patch is rejected.

A record that is absent or owned by another user returns `404 Not Found`.

### Delete an observation

`DELETE /api/v1/observations/{id}`

Only the owner can delete it. Success returns `204 No Content`. A record that is absent or owned by another user returns `404 Not Found`.

## Visibility and location privacy

The `visibility` value is stored, but cross-user public or limited browsing is not part of the current read API: list and detail remain owner-scoped. Do not treat a stored `public` value as evidence that a public read endpoint exists.

Location is not returned by this API slice. Exact coordinate storage and generalized public location responses require a separate implementation and privacy tests.

## Security and tests

- Never accept an owner ID from the request body.
- Apply owner filtering in database reads and writes.
- Keep `404` behavior consistent for missing and non-owned records.
- Retain CSRF protection for session-authenticated writes.
- Tests cover unauthenticated access, private-by-default creation, owner-scoped reads, cross-user patch/delete denial, validation, and deletion.