# ERD

## Core

```text
users
  1 ─── N observations

observations
  1 ─── N observation_media
  1 ─── N identifications
  1 ─── N predictions (AI outputs)
  1 ─── 0..1 observation_locations
  N ─── 0..1 taxa (observer-selected current taxon)

identifications
  N ─── 1 users
  N ─── 1 taxa
  1 ─── N expert_reviews (reviews of human identifications)

projects
  1 ─── N project_members
  1 ─── N project_observations

missions
  1 ─── N mission_observations
```

## 주요 테이블

### users
- id PK
- username
- email
- password_hash
- role
- created_at
- updated_at

### observations
- id PK
- observer_id FK
- taxon_id FK nullable
- title nullable
- description
- observed_at
- status
- visibility
- license_id
- created_at
- updated_at

### observation_locations
- observation_id PK/FK
- geom geography(Point,4326)
- positional_accuracy_m
- geoprivacy
- public_geom
- location_source

### observation_media
- id PK
- observation_id FK
- storage_key
- media_type
- mime_type
- width
- height
- duration
- alt_text
- license_id

### taxa
- id PK
- parent_id FK nullable
- scientific_name
- rank
- canonical_name
- korean_name
- source
- source_taxon_id

### identifications
- id PK
- observation_id FK
- user_id FK
- taxon_id FK
- source_type
- confidence nullable
- comment
- created_at

### data_quality_assessments
- id PK
- observation_id FK
- completeness_score
- media_quality_score
- location_quality_score
- identification_consensus_score
- temporal_quality_score
- flags
- calculated_at

## MVP clarifications

- `observations.taxon_id` is the observer-selected current taxon; it is separate from identification history.
- Human/community assertions are stored as identifications. AI outputs are predictions, not identifications.
- Expert reviews refer to human identification assertions.
- An observation may have no location. `visibility` controls observation access; `geoprivacy` controls location precision.
- Environment fields, quality-score scales, license catalogs, and observation status values remain deferred until their rules are defined.
