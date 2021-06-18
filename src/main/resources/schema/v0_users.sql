CREATE TABLE users
(
    id         SERIAL PRIMARY KEY,
    login      TEXT        NOT NULL UNIQUE,
    first_name TEXT,
    last_name  TEXT,
    email      TEXT        NOT NULL UNIQUE,
    phone      TEXT        UNIQUE,
    about      TEXT,
    avatar     BYTEA,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-------------------------------------------------------------------------

CREATE TABLE user_accounts
(
    user_id        INTEGER NOT NULL UNIQUE REFERENCES users (id) ON DELETE CASCADE,
    password       TEXT    NOT NULL,
    last_auth_time TIMESTAMPTZ
)
