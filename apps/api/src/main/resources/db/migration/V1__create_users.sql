CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR(32) NOT NULL,
    email VARCHAR(320) NOT NULL,
    password_hash VARCHAR(100) NOT NULL,
    role VARCHAR(32) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_users_username CHECK (username ~ '^[A-Za-z0-9_]{3,32}$'),
    CONSTRAINT ck_users_email_lowercase CHECK (email = LOWER(email))
);

CREATE UNIQUE INDEX uk_users_username_lower ON users (LOWER(username));
CREATE UNIQUE INDEX uk_users_email_lower ON users (LOWER(email));