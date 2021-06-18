CREATE TYPE COMMUNITY_TYPE AS ENUM (
    'ORGANIZATION', 'CLUB'
    );

CREATE TABLE communities
(
    id             SERIAL PRIMARY KEY,
    owner_user_id  INTEGER        NOT NULL REFERENCES users (id) ON DELETE RESTRICT,
    name           TEXT           NOT NULL,
    nickname       TEXT           NOT NULL UNIQUE,
    avatar         BYTEA,
    community_type COMMUNITY_TYPE NOT NULL,
    sport_types    SPORT_TYPE[]   NOT NULL,
    user_ids       INTEGER[],
    url            TEXT,
    description    TEXT,
    created_at     TIMESTAMPTZ    NOT NULL DEFAULT now()
);
