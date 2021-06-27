CREATE TABLE trackers
(
    id      SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    imei    TEXT    NOT NULL UNIQUE
);

CREATE TABLE telemetries
(
    tracker_id  INTEGER     NOT NULL,
    fix_time    TIMESTAMPTZ CHECK (fix_time < now() + INTERVAL '1 minute'),
    server_time TIMESTAMPTZ NOT NULL DEFAULT now(),
    latitude    DOUBLE PRECISION CHECK (latitude > -90 AND latitude < 90),
    longitude   DOUBLE PRECISION CHECK (longitude > -180 AND longitude < 180),
    altitude    DOUBLE PRECISION,
    speed       SMALLINT CHECK (speed >= 0),
    course      SMALLINT CHECK (course >= 0 AND course < 360),

    CONSTRAINT telemetries_pkey PRIMARY KEY (tracker_id, fix_time)
);
