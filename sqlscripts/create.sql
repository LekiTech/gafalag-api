-- region EXTENSIONS
CREATE EXTENSION IF NOT EXISTS pg_trgm; -- for fuzzy search

CREATE EXTENSION IF NOT EXISTS "uuid-ossp"; -- for id
-- endregion

CREATE TYPE MEDIATYPE AS ENUM ('AUDIO','IMAGE', 'VIDEO');

CREATE TYPE TAGTYPE AS ENUM ('CATEGORY','GRAMMAR', 'ETYMOLOGY', 'UNKNOWN');

-- region TABLES
CREATE TABLE expression (
    id          UUID PRIMARY KEY     DEFAULT uuid_generate_v4(),
    spelling    VARCHAR     NOT NULL,
    -- inflection  VARCHAR,
    details_id  UUID        NOT NULL,
    language_id VARCHAR     NOT NULL,
    -- dialect_id  INT,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE expression_details (
    id             UUID PRIMARY KEY     DEFAULT uuid_generate_v4(),
    gr             VARCHAR     NOT NULL,
    inflection     VARCHAR,
    expression_id  UUID     NOT NULL,
    source_id      UUID     NOT NULL,
    -- dialect_id  INT,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE definition_details (
    id                UUID PRIMARY KEY     DEFAULT uuid_generate_v4(),
    expression_details_id     UUID        NOT NULL,
    language_id       VARCHAR     NOT NULL,
    dialect_id        INT        NOT NULL,
    created_at        TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at        TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE definition (
    id                UUID PRIMARY KEY     DEFAULT uuid_generate_v4(),
    value   VARCHAR     NOT NULL,
--    tags   VARCHAR     NOT NULL,
    definition_details_id     UUID        NOT NULL,
    created_at        TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at        TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE tag (
--    id            UUID PRIMARY KEY     DEFAULT uuid_generate_v4(),
    -- e.g. 'см.тж.', 'сущ.', 'диал.'
    abbreviation  VARCHAR(10)     PRIMARY KEY,
    -- e.g. 'смотри также', 'имя  существительное', 'диалектизм'
    description   VARCHAR,
    tagtype       TAGTYPE     NOT NULL,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE definition_tag (
    tag_abbr          VARCHAR(10)        NOT NULL,
    definition_id     UUID        NOT NULL,
    created_at        TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE example (
    id                UUID PRIMARY KEY     DEFAULT uuid_generate_v4(),
    source            VARCHAR,
    translation       VARCHAR,
    src_lang_id       VARCHAR     NOT NULL,
    trl_lang_id       VARCHAR,
    raw               VARCHAR     NOT NULL,
    created_at        TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at        TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE expression_example (
    expression_details_id     UUID        NOT NULL,
    example_id                UUID        NOT NULL,
    created_at                TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE definition_example (
    example_id                UUID        NOT NULL,
    definition_details_id     UUID        NOT NULL,
    created_at                TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE language (
    id         VARCHAR(3) PRIMARY KEY, -- iso639 alpha 3
    name       VARCHAR     NOT NULL UNIQUE,
    iso_2      VARCHAR(2) UNIQUE,      -- iso639 alpha 2
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE dialect (
    id          SERIAL PRIMARY KEY,
    language_id VARCHAR     NOT NULL,
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
    language_id    VARCHAR     NOT NULL,
    dialect_id     INT,
    etymology_text VARCHAR     NOT NULL UNIQUE DEFAULT 'unknown',
    created_at     TIMESTAMPTZ NOT NULL        DEFAULT now(),
    updated_at     TIMESTAMPTZ NOT NULL        DEFAULT now()
);
-- endregion TABLES

-- region ALTER TABLES
ALTER TABLE expression
    ADD FOREIGN KEY (details_id) REFERENCES expression_details (id),
    ADD FOREIGN KEY (language_id) REFERENCES language (id);

ALTER TABLE expression_details
    ADD FOREIGN KEY (expression_id) REFERENCES expression (id),
    ADD FOREIGN KEY (source_id) REFERENCES source (id);

ALTER TABLE definition_details
    ADD FOREIGN KEY (expression_details_id) REFERENCES expression_details (id),
    ADD FOREIGN KEY (language_id) REFERENCES language (id),
    ADD FOREIGN KEY (dialect_id) REFERENCES dialect (id);

ALTER TABLE definition
    ADD FOREIGN KEY (definition_details_id) REFERENCES definition_details (id);

ALTER TABLE definition_tag
    ADD FOREIGN KEY (definition_id) REFERENCES definition (id),
    ADD FOREIGN KEY (tag_abbr) REFERENCES tag (abbreviation);

ALTER TABLE example
    ADD FOREIGN KEY (src_lang_id) REFERENCES language (id),
    ADD FOREIGN KEY (trl_lang_id) REFERENCES language (id);

ALTER TABLE expression_example
    ADD FOREIGN KEY (expression_details_id) REFERENCES expression_details (id),
    ADD FOREIGN KEY (example_id) REFERENCES example (id);

ALTER TABLE definition_example
    ADD FOREIGN KEY (example_id) REFERENCES example (id),
    ADD FOREIGN KEY (definition_details_id) REFERENCES definition_details (id);

ALTER TABLE dialect
    ADD FOREIGN KEY (language_id) REFERENCES language (id);

ALTER TABLE mediafile
    ADD FOREIGN KEY (expression_id) REFERENCES expression (id);

ALTER TABLE expression_relation
    ADD PRIMARY KEY (expression_1_id, expression_2_id),
    ADD FOREIGN KEY (expression_1_id) REFERENCES expression (id),
    ADD FOREIGN KEY (expression_2_id) REFERENCES expression (id),
    ADD FOREIGN KEY (relation_type_id) REFERENCES relation_type (id);

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

CREATE TRIGGER example_updated
    BEFORE UPDATE
    ON example
    FOR EACH ROW
EXECUTE PROCEDURE set_current_timestamp();

CREATE TRIGGER expression_example_updated
    BEFORE UPDATE
    ON expression_example
    FOR EACH ROW
EXECUTE PROCEDURE set_current_timestamp();

CREATE TRIGGER definition_example_updated
    BEFORE UPDATE
    ON definition_example
    FOR EACH ROW
EXECUTE PROCEDURE set_current_timestamp();

CREATE TRIGGER expression_details_updated
    BEFORE UPDATE
    ON expression_details
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

CREATE TRIGGER definition_updated
    BEFORE UPDATE
    ON definition
    FOR EACH ROW
EXECUTE PROCEDURE set_current_timestamp();

CREATE TRIGGER definition_details_updated
    BEFORE UPDATE
    ON definition_details
    FOR EACH ROW
EXECUTE PROCEDURE set_current_timestamp();

CREATE TRIGGER definition_tag_updated
    BEFORE UPDATE
    ON definition_tag
    FOR EACH ROW
EXECUTE PROCEDURE set_current_timestamp();

CREATE TRIGGER tag_updated
    BEFORE UPDATE
    ON tag
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