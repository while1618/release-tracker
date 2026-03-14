-- ============================================================
-- V1: Create releases table
-- ============================================================

CREATE TABLE releases (
    id              BIGINT          GENERATED ALWAYS AS IDENTITY,
    name            VARCHAR(255)    NOT NULL,
    description     TEXT,
    status          VARCHAR(50)     NOT NULL DEFAULT 'CREATED',
    release_date    DATE,
    created_at      TIMESTAMPTZ     NOT NULL DEFAULT now(),
    last_updated_at TIMESTAMPTZ     NOT NULL DEFAULT now(),

    CONSTRAINT pk_releases PRIMARY KEY (id),
    CONSTRAINT chk_releases_status CHECK (
        status IN (
            'CREATED',
            'IN_DEVELOPMENT',
            'ON_DEV',
            'QA_DONE_ON_DEV',
            'ON_STAGING',
            'QA_DONE_ON_STAGING',
            'ON_PROD',
            'DONE'
        )
    )
);

CREATE INDEX idx_releases_status       ON releases (status);
CREATE INDEX idx_releases_release_date ON releases (release_date);
CREATE INDEX idx_releases_name         ON releases (name);
