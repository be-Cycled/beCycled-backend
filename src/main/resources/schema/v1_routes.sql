CREATE TYPE SPORT_TYPE AS ENUM (
    'BICYCLE', 'ROLLERBLADE','RUN', 'SKI'
    );

CREATE TABLE routes
(
    id          SERIAL PRIMARY KEY,
    user_id     INTEGER      NOT NULL REFERENCES users (id) ON DELETE RESTRICT,
    name        TEXT,
    route_info  TEXT         NOT NULL,
    sport_types SPORT_TYPE[] NOT NULL,
    disposable  BOOLEAN      NOT NULL,
    description TEXT,
    popularity  INTEGER      NOT NULL DEFAULT 0,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now()
);

-------------------------------------------------------------------------

CREATE TABLE route_photos
(
    id         SERIAL PRIMARY KEY,
    route_id   INTEGER     NOT NULL REFERENCES routes (id) ON DELETE CASCADE,
    photo      TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
