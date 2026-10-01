CREATE TABLE taxa (
    id UUID PRIMARY KEY,
    parent_id UUID REFERENCES taxa(id) ON DELETE SET NULL,
    scientific_name TEXT NOT NULL CHECK (BTRIM(scientific_name) <> ''),
    rank VARCHAR(40) NOT NULL CHECK (BTRIM(rank) <> ''),
    canonical_name TEXT NOT NULL CHECK (BTRIM(canonical_name) <> ''),
    korean_name TEXT,
    source VARCHAR(100) NOT NULL CHECK (BTRIM(source) <> ''),
    source_taxon_id VARCHAR(255),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX uk_taxa_source_taxon
    ON taxa (source, source_taxon_id)
    WHERE source_taxon_id IS NOT NULL;

CREATE INDEX ix_taxa_parent_id ON taxa (parent_id);
CREATE INDEX ix_taxa_scientific_name ON taxa (scientific_name);

CREATE TABLE observations (
    id UUID PRIMARY KEY,
    observer_id UUID NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    taxon_id UUID REFERENCES taxa(id) ON DELETE SET NULL,
    title TEXT,
    description TEXT,
    observed_at TIMESTAMPTZ NOT NULL,
    visibility VARCHAR(16) NOT NULL DEFAULT 'private',
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_observations_visibility
        CHECK (visibility IN ('public', 'limited', 'private'))
);

CREATE INDEX ix_observations_observer_created
    ON observations (observer_id, created_at DESC);

CREATE INDEX ix_observations_visibility_created
    ON observations (visibility, created_at DESC);

CREATE INDEX ix_observations_taxon_id
    ON observations (taxon_id);

CREATE TABLE observation_locations (
    observation_id UUID PRIMARY KEY
        REFERENCES observations(id) ON DELETE CASCADE,
    geom GEOGRAPHY(POINT, 4326) NOT NULL,
    positional_accuracy_m NUMERIC(8, 2),
    geoprivacy VARCHAR(16) NOT NULL DEFAULT 'private',
    public_geom GEOGRAPHY(POINT, 4326),
    location_source VARCHAR(40),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_observation_locations_accuracy
        CHECK (positional_accuracy_m IS NULL OR positional_accuracy_m >= 0),
    CONSTRAINT ck_observation_locations_geoprivacy
        CHECK (geoprivacy IN ('open', 'obscured', 'private'))
);

CREATE INDEX ix_observation_locations_geom_gist
    ON observation_locations USING GIST (geom);

CREATE INDEX ix_observation_locations_public_geom_gist
    ON observation_locations USING GIST (public_geom);

CREATE TABLE observation_media (
    id UUID PRIMARY KEY,
    observation_id UUID NOT NULL
        REFERENCES observations(id) ON DELETE CASCADE,
    storage_key VARCHAR(1024) NOT NULL UNIQUE,
    media_type VARCHAR(16) NOT NULL,
    mime_type VARCHAR(128) NOT NULL,
    width INTEGER,
    height INTEGER,
    duration NUMERIC(12, 3),
    alt_text TEXT,
    inspection_status VARCHAR(16) NOT NULL DEFAULT 'pending',
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_observation_media_type
        CHECK (media_type IN ('image', 'video', 'audio')),
    CONSTRAINT ck_observation_media_width
        CHECK (width IS NULL OR width > 0),
    CONSTRAINT ck_observation_media_height
        CHECK (height IS NULL OR height > 0),
    CONSTRAINT ck_observation_media_duration
        CHECK (duration IS NULL OR duration >= 0),
    CONSTRAINT ck_observation_media_inspection
        CHECK (inspection_status IN ('pending', 'accepted', 'rejected'))
);

CREATE INDEX ix_observation_media_observation
    ON observation_media (observation_id, created_at);

CREATE TABLE identifications (
    id UUID PRIMARY KEY,
    observation_id UUID NOT NULL
        REFERENCES observations(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    taxon_id UUID NOT NULL REFERENCES taxa(id) ON DELETE RESTRICT,
    source_type VARCHAR(16) NOT NULL,
    confidence NUMERIC(6, 5),
    comment TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_identifications_source_type
        CHECK (source_type IN ('human', 'community')),
    CONSTRAINT ck_identifications_confidence
        CHECK (confidence IS NULL OR confidence BETWEEN 0 AND 1)
);

CREATE INDEX ix_identifications_observation_created
    ON identifications (observation_id, created_at DESC);

CREATE INDEX ix_identifications_taxon_id
    ON identifications (taxon_id);

CREATE TABLE predictions (
    id UUID PRIMARY KEY,
    observation_id UUID NOT NULL
        REFERENCES observations(id) ON DELETE CASCADE,
    taxon_id UUID NOT NULL REFERENCES taxa(id) ON DELETE RESTRICT,
    model_name VARCHAR(120) NOT NULL,
    model_version VARCHAR(120) NOT NULL,
    confidence NUMERIC(6, 5),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_predictions_confidence
        CHECK (confidence IS NULL OR confidence BETWEEN 0 AND 1)
);

CREATE INDEX ix_predictions_observation_created
    ON predictions (observation_id, created_at DESC);

CREATE TABLE expert_reviews (
    id UUID PRIMARY KEY,
    identification_id UUID NOT NULL
        REFERENCES identifications(id) ON DELETE CASCADE,
    reviewer_user_id UUID NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    decision VARCHAR(40) NOT NULL CHECK (BTRIM(decision) <> ''),
    comment TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX ix_expert_reviews_identification_created
    ON expert_reviews (identification_id, created_at DESC);