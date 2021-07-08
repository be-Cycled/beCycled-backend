CREATE TYPE COMMUNITY_TYPE AS ENUM (
    'ORGANIZATION', 'CLUB'
    );

CREATE TABLE communities
(
    id             SERIAL PRIMARY KEY,
    owner_user_id  INTEGER        NOT NULL REFERENCES users (id) ON DELETE RESTRICT,
    name           TEXT           NOT NULL,
    nickname       TEXT           NOT NULL UNIQUE,
    avatar         TEXT,
    community_type COMMUNITY_TYPE NOT NULL,
    sport_types    SPORT_TYPE[]   NOT NULL,
    url            TEXT,
    description    TEXT,
    created_at     TIMESTAMPTZ    NOT NULL DEFAULT now()
);

CREATE TABLE community_members
(
    community_id INTEGER NOT NULL REFERENCES communities (id) ON DELETE CASCADE,
    user_id      INTEGER NOT NULL REFERENCES users (id) ON DELETE CASCADE,

    CONSTRAINT unique_community_id_and_user_id UNIQUE (community_id, user_id)
);
