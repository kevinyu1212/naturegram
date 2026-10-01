# ADR-06: Observation Data Model Decisions

- Status: Accepted for the Observation MVP
- Scope: Observation, taxonomy, location privacy, identification, licensing, and quality data

## Decisions

### 1. Canonical names and scope

Use `observation_locations` consistently in the ERD and database.

The first observation migration includes only fields whose meaning and constraints are defined. `observation_environment` and quality-assessment score ranges are deferred until their fields and scoring rules are documented.

### 2. Observation taxon and identification history

`observations.taxon_id` is the observer-selected current taxon and is nullable.

Human identification records are separate, append-only assertions. Adding or changing an identification must not silently overwrite the observation's selected taxon or erase prior identifications.

### 3. AI and expert decisions

AI outputs are stored as `predictions`, not as human identifications.

Human/community identification assertions are stored as `identifications`. Expert decisions are stored as `expert_reviews` and retain reviewer, decision, and timestamp. AI confidence, community consensus, expert review, and metadata completeness remain distinct signals.

### 4. Observation visibility and location privacy

`observations.visibility` controls who may view an observation (`public`, `limited`, or `private`).

`observation_locations.geoprivacy` controls location precision (`open`, `obscured`, or `private`). Exact coordinates are restricted. Public responses must use a separately generated public geometry or omit coordinates; they must not expose the exact geometry by serializing the persistence entity.

### 5. Licenses and research permission

Observation-data licensing and media licensing are separate concepts and references.

A license reference describes reuse terms; permission for a particular research use is a separate consent/policy decision and must not be inferred solely from a license identifier. The license catalog and required/default selection must be defined before the observation migration adds non-null license foreign keys.

### 6. Taxonomy and export

Taxa use stable internal UUIDs and retain external source identifiers and source metadata. Darwin Core compatibility is implemented through a versioned export mapping, not by making the internal schema identical to the export format.

### 7. Data quality

Quality dimensions remain separate. Do not define a single score as scientific accuracy. Score scales and calculation rules must be documented before database constraints or user-facing thresholds are added.

## Security and acceptance requirements

- All observation writes require an authenticated owner and server-side ownership checks.
- Private observations are not accessible to other users.
- Exact coordinates are never returned in public DTOs.
- Predictions cannot be represented as expert-verified identifications.
- License terms and research-use permission are not conflated.
- Database constraints and API validation match the documented enum values and nullability.
- Integration tests cover cross-user access and location non-disclosure.