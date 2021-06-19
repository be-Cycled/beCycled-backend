CREATE TABLE workouts
(
    id            SERIAL PRIMARY KEY,
    owner_user_id INTEGER     NOT NULL REFERENCES users (id) ON DELETE RESTRICT,
    community_id  INTEGER REFERENCES communities (id) ON DELETE CASCADE,
    private       BOOLEAN     NOT NULL,
    start_date    TIMESTAMPTZ NOT NULL,
    route_id      INTEGER     NOT NULL REFERENCES routes (id),
    sport_types   SPORT_TYPE[] NOT NULL,
    user_ids      INTEGER[],
    venue         TEXT,
    duration      INTEGER,
    description   TEXT,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

-------------------------------------------------------------------------

CREATE TABLE competitions
(
    id            SERIAL PRIMARY KEY,
    owner_user_id INTEGER     NOT NULL REFERENCES users (id) ON DELETE RESTRICT,
    community_id  INTEGER REFERENCES communities (id) ON DELETE CASCADE,
    private       BOOLEAN     NOT NULL,
    start_date    TIMESTAMPTZ NOT NULL,
    route_id      INTEGER     NOT NULL REFERENCES routes (id),
    sport_types   SPORT_TYPE[] NOT NULL,
    user_ids      INTEGER[],
    description   TEXT,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);
