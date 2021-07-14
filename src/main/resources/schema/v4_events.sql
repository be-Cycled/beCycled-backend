CREATE TYPE EVENT_TYPE AS ENUM (
    'WORKOUT', 'COMPETITION'
    );

CREATE TABLE events
(
    id              SERIAL PRIMARY KEY,
    owner_user_id   INTEGER     NOT NULL REFERENCES users (id) ON DELETE RESTRICT,
    community_id    INTEGER              REFERENCES communities (id) ON DELETE CASCADE,
    event_type      EVENT_TYPE  NOT NULL,
    private         BOOLEAN     NOT NULL,
    start_date      TIMESTAMPTZ NOT NULL,
    route_id        INTEGER     NOT NULL REFERENCES routes (id),
    sport_type      SPORT_TYPE  NOT NULL,
    venue_geo_data  TEXT,
    duration        INTEGER,
    description     TEXT,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    attributes      JSONB       NOT NULL DEFAULT '{}'::JSONB
);

CREATE TABLE event_members
(
    event_id   INTEGER NOT NULL REFERENCES events (id) ON DELETE CASCADE,
    user_id    INTEGER NOT NULL REFERENCES users (id) ON DELETE CASCADE,

    CONSTRAINT unique_event_id_and_user_id UNIQUE (event_id, user_id)
);
