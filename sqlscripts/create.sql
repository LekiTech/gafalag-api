-- region EXTENSIONS
CREATE EXTENSION IF NOT EXISTS pg_trgm; -- for fuzzy search

CREATE EXTENSION IF NOT EXISTS "uuid-ossp"; -- for id
-- endregion

CREATE TYPE MEDIATYPE AS ENUM ('AUDIO','IMAGE');

-- region TABLES
CREATE TABLE expression (
    id          UUID PRIMARY KEY     DEFAULT uuid_generate_v4(),
    spelling    VARCHAR     NOT NULL,
    misspelling BOOLEAN              DEFAULT FALSE,
    inflection  VARCHAR,
    gender_id   INT,
    language_id INT         NOT NULL,
    dialect_id  INT,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE gender (
    id         SERIAL PRIMARY KEY,
    name       VARCHAR     NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE part_of_speech (
    id         SERIAL PRIMARY KEY,
    name       VARCHAR     NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE language (
    id         SERIAL PRIMARY KEY,
    name       VARCHAR     NOT NULL UNIQUE,
    iso639_2   VARCHAR(2) UNIQUE,
    iso639_3   VARCHAR(3) UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE dialect (
    id          SERIAL PRIMARY KEY,
    language_id INT         NOT NULL,
    name        VARCHAR     NOT NULL UNIQUE,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE mediafile (
    id            SERIAL PRIMARY KEY,
    expression_id UUID        NOT NULL,
    mediatype     MEDIATYPE   NOT NULL,
    url           VARCHAR     NOT NULL,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE expression_relation (
    expression_1_id  UUID        NOT NULL,
    expression_2_id  UUID        NOT NULL,
    relation_type_id INT         NOT NULL,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE relation_type (
    id         SERIAL PRIMARY KEY,
    name       VARCHAR     NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE definition_category (
    definition_id UUID        NOT NULL,
    category_id   UUID        NOT NULL,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE category (
    id         UUID PRIMARY KEY     DEFAULT uuid_generate_v4(),
    name       VARCHAR     NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE definition (
    id                UUID PRIMARY KEY     DEFAULT uuid_generate_v4(),
    expression_id     UUID        NOT NULL,
    part_of_speech_id INT,
    language_id       INT         NOT NULL,
    source_id         UUID        NOT NULL,
    definition_text   VARCHAR     NOT NULL,
    created_at        TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at        TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE source (
    id         UUID PRIMARY KEY     DEFAULT uuid_generate_v4(),
    name       VARCHAR     NOT NULL UNIQUE,
    url        VARCHAR,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE etymology (
    id             UUID PRIMARY KEY            DEFAULT uuid_generate_v4(),
    expression_id  UUID        NOT NULL,
    language_id    INT         NOT NULL,
    dialect_id     INT,
    etymology_text VARCHAR     NOT NULL UNIQUE DEFAULT 'unknown',
    created_at     TIMESTAMPTZ NOT NULL        DEFAULT now(),
    updated_at     TIMESTAMPTZ NOT NULL        DEFAULT now()
);
-- endregion TABLES

-- region ALTER TABLES
ALTER TABLE expression
    ADD FOREIGN KEY (gender_id) REFERENCES gender (id),
    ADD FOREIGN KEY (language_id) REFERENCES language (id),
    ADD FOREIGN KEY (dialect_id) REFERENCES dialect (id);

ALTER TABLE dialect
    ADD FOREIGN KEY (language_id) REFERENCES language (id);

ALTER TABLE mediafile
    ADD FOREIGN KEY (expression_id) REFERENCES expression (id);

ALTER TABLE expression_relation
    ADD PRIMARY KEY (expression_1_id, expression_2_id),
    ADD FOREIGN KEY (expression_1_id) REFERENCES expression (id),
    ADD FOREIGN KEY (expression_2_id) REFERENCES expression (id),
    ADD FOREIGN KEY (relation_type_id) REFERENCES relation_type (id);

ALTER TABLE definition_category
    ADD PRIMARY KEY (definition_id, category_id),
    ADD FOREIGN KEY (definition_id) REFERENCES definition (id),
    ADD FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE definition
    ADD FOREIGN KEY (expression_id) REFERENCES expression (id),
    ADD FOREIGN KEY (part_of_speech_id) REFERENCES part_of_speech (id),
    ADD FOREIGN KEY (language_id) REFERENCES language (id),
    ADD FOREIGN KEY (source_id) REFERENCES source (id);

ALTER TABLE etymology
    ADD FOREIGN KEY (expression_id) REFERENCES expression (id),
    ADD FOREIGN KEY (language_id) REFERENCES language (id),
    ADD FOREIGN KEY (dialect_id) REFERENCES dialect (id);
-- endregion ALTER TABLES

-- region FUNCTIONS
CREATE OR REPLACE FUNCTION set_current_timestamp()
    RETURNS TRIGGER AS
$$
BEGIN
    new.updated_at = now();
    RETURN new;
END
$$ LANGUAGE plpgsql;
-- endregion FUNCTIONS

-- region TRIGGERS
CREATE TRIGGER expression_updated
    BEFORE UPDATE
    ON expression
    FOR EACH ROW
EXECUTE PROCEDURE set_current_timestamp();

CREATE TRIGGER gender_updated
    BEFORE UPDATE
    ON gender
    FOR EACH ROW
EXECUTE PROCEDURE set_current_timestamp();

CREATE TRIGGER part_of_speech_updated
    BEFORE UPDATE
    ON part_of_speech
    FOR EACH ROW
EXECUTE PROCEDURE set_current_timestamp();

CREATE TRIGGER language_updated
    BEFORE UPDATE
    ON language
    FOR EACH ROW
EXECUTE PROCEDURE set_current_timestamp();

CREATE TRIGGER dialect_updated
    BEFORE UPDATE
    ON dialect
    FOR EACH ROW
EXECUTE PROCEDURE set_current_timestamp();

CREATE TRIGGER mediafile_updated
    BEFORE UPDATE
    ON mediafile
    FOR EACH ROW
EXECUTE PROCEDURE set_current_timestamp();

CREATE TRIGGER relation_type_updated
    BEFORE UPDATE
    ON relation_type
    FOR EACH ROW
EXECUTE PROCEDURE set_current_timestamp();

CREATE TRIGGER category_updated
    BEFORE UPDATE
    ON category
    FOR EACH ROW
EXECUTE PROCEDURE set_current_timestamp();

CREATE TRIGGER definition_updated
    BEFORE UPDATE
    ON definition
    FOR EACH ROW
EXECUTE PROCEDURE set_current_timestamp();

CREATE TRIGGER source_updated
    BEFORE UPDATE
    ON source
    FOR EACH ROW
EXECUTE PROCEDURE set_current_timestamp();

CREATE TRIGGER etymology_updated
    BEFORE UPDATE
    ON etymology
    FOR EACH ROW
EXECUTE PROCEDURE set_current_timestamp();
-- endregion TRIGGERS

-- region INDEXES
CREATE INDEX exp_gin_idx ON expression USING gin (spelling gin_trgm_ops);
-- endregion