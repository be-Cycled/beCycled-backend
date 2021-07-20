CREATE TYPE EVENT_TYPE AS ENUM (
    'RUN_WORKOUT', 'RUN_COMPETITION',
    'BICYCLE_WORKOUT', 'BICYCLE_COMPETITION',
    'ROLLERBLADE_WORKOUT', 'ROLLERBLADE_COMPETITION'
    );

CREATE TABLE events
(
    id             SERIAL PRIMARY KEY,
    owner_user_id  INTEGER     NOT NULL REFERENCES users (id) ON DELETE RESTRICT,
    community_id   INTEGER REFERENCES communities (id) ON DELETE RESTRICT,
    event_type     EVENT_TYPE  NOT NULL,
    start_date     TIMESTAMPTZ NOT NULL,
    duration       INTEGER     NOT NULL,
    description    TEXT        NOT NULL,
    url            TEXT        NOT NULL,
    route_id       INTEGER     NOT NULL REFERENCES routes (id) ON DELETE RESTRICT,
    venue_geo_data TEXT,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    attributes     JSONB       NOT NULL DEFAULT '{}'::JSONB
);

CREATE TABLE event_members
(
    event_id INTEGER NOT NULL REFERENCES events (id) ON DELETE CASCADE,
    user_id  INTEGER NOT NULL REFERENCES users (id) ON DELETE CASCADE,

    CONSTRAINT unique_event_id_and_user_id UNIQUE (event_id, user_id)
);
