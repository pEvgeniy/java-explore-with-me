DROP TABLE IF EXISTS users, categories, events, compilations, compilation_events, locations, requests, comments CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    id    INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    name  VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS categories
(
    id   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(128) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS locations
(
    id  INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    lat FLOAT NOT NULL,
    lon FLOAT NOT NULL
);

CREATE TABLE IF NOT EXISTS comments
(
    id   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    text VARCHAR(512) NOT NULL
);

CREATE TABLE IF NOT EXISTS events
(
    id                 INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    annotation         VARCHAR(2024)               NOT NULL,
    category_id        INTEGER REFERENCES categories (id) ON DELETE CASCADE,
    confirmed_requests INTEGER,
    created_on         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    description        VARCHAR(7024)               NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    initiator_id       INTEGER REFERENCES users (id) ON DELETE CASCADE,
    location_id        BIGINT                      NOT NULL REFERENCES locations (id),
    paid               BOOLEAN                     NOT NULL DEFAULT false,
    participant_limit  INTEGER                     NOT NULL DEFAULT 0,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN,
    state              VARCHAR(255)                         DEFAULT 'PENDING',
    title              VARCHAR(255)                NOT NULL,
    comment_id         INTEGER REFERENCES comments (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    pinned BOOLEAN      NOT NULL DEFAULT false,
    title  VARCHAR(128) NOT NULL
);

CREATE TABLE IF NOT EXISTS requests
(
    id           INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    event_id     INTEGER      NOT NULL REFERENCES events (id) ON DELETE CASCADE,
    requester_id INTEGER      NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    status       VARCHAR(255) NOT NULL DEFAULT 'PENDING',
    created_date TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE IF NOT EXISTS compilation_events
(
    compilation_id INTEGER REFERENCES compilations (id) ON DELETE CASCADE,
    event_id       INTEGER REFERENCES events (id) ON DELETE CASCADE,
    PRIMARY KEY (compilation_id, event_id)
);