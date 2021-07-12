CREATE TABLE workouts
(
    id              SERIAL PRIMARY KEY,
    owner_user_id   INTEGER     NOT NULL REFERENCES users (id) ON DELETE RESTRICT,
    community_id    INTEGER              REFERENCES communities (id) ON DELETE CASCADE,
    private         BOOLEAN     NOT NULL,
    start_date      TIMESTAMPTZ NOT NULL,
    route_id        INTEGER     NOT NULL REFERENCES routes (id),
    sport_type      SPORT_TYPE  NOT NULL,
    venue_geo_data  TEXT,
    duration        INTEGER,
    description     TEXT,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE workout_members
(
    workout_id INTEGER NOT NULL REFERENCES workouts (id) ON DELETE CASCADE,
    user_id    INTEGER NOT NULL REFERENCES users (id) ON DELETE CASCADE,

    CONSTRAINT unique_workout_id_and_user_id UNIQUE (workout_id, user_id)
);

-------------------------------------------------------------------------

CREATE TABLE competitions
(
    id             SERIAL PRIMARY KEY,
    owner_user_id  INTEGER     NOT NULL REFERENCES users (id) ON DELETE RESTRICT,
    community_id   INTEGER              REFERENCES communities (id) ON DELETE CASCADE,
    private        BOOLEAN     NOT NULL,
    start_date     TIMESTAMPTZ NOT NULL,
    route_id       INTEGER     NOT NULL REFERENCES routes (id),
    sport_type     SPORT_TYPE  NOT NULL,
    venue_geo_data TEXT,
    duration       INTEGER,
    description    TEXT,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE competition_members
(
    competition_id INTEGER NOT NULL REFERENCES competitions (id) ON DELETE CASCADE,
    user_id        INTEGER NOT NULL REFERENCES users (id) ON DELETE CASCADE,

    CONSTRAINT unique_competition_id_and_user_id UNIQUE (competition_id, user_id)
);
