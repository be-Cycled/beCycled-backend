CREATE TABLE posts
(
    id         SERIAL PRIMARY KEY,
    user_id    INTEGER     NOT NULL REFERENCES users (id) ON DELETE RESTRICT,
    title      TEXT        NOT NULL,
    content    TEXT        NOT NULL,
    poster     TEXT        NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ
);
