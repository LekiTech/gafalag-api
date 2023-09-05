-- region EXTENSIONS
CREATE EXTENSION IF NOT EXISTS pg_trgm; -- for fuzzy search

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
-- for id
-- endregion

CREATE TYPE MEDIA_TYPE AS ENUM (
    'AUDIO',
    'IMAGE',
    'VIDEO'
    );

CREATE TYPE TAG_TYPE AS ENUM (
    'CATEGORY',
    'GRAMMAR',
    'ETYMOLOGY',
    'UNKNOWN'
    );

CREATE TYPE SOURCE_TYPE AS ENUM (
    'WRITTEN',
    'USER'
    );

-- Grammatical number
-- Грамматическое число
-- based on https://en.wikipedia.org/wiki/Grammatical_number
CREATE TYPE GRAMM_NUMBER AS ENUM (
    'NONE',
    -- 3 AND more
    'PLURAL_MANY',
    -- exact 1
    'SINGLE_1',
    -- few
    'PAUCAL_FEW',
    -- exact 2
    'DUAL_2',
    -- exact 3
    'TRIAL_3',
    -- exact 4
    'QUADRAL_4',
    'DISTR_PLURAL',
    'SUPERPLURAL'
    );

-- Grammatical person
-- Лицо (лингвистика)
-- based on https://en.wikipedia.org/wiki/Grammatical_person
CREATE TYPE GRAMM_PERSON AS ENUM (
    -- I, we [depends on gramm. number]
    'FIRST',
    -- you, (ты, вы) [depends on gramm. number]
    'SECOND',
    -- he, she, it, they [depends on gramm. number and gender]
    'THIRD'
    );

CREATE TYPE GENDER AS ENUM (
    -- e.g. he
    'MALE',
    -- e.g. she
    'FEMALE',
    -- e.g. it
    'NONE',
    -- e.g. they
    'BOTH'
    );

-- region TABLES
CREATE TABLE expression
(
    id          UUID PRIMARY KEY     DEFAULT uuid_generate_v4(),
    spelling    VARCHAR     NOT NULL,
    details_id  UUID        NOT NULL,
    language_id VARCHAR     NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE expression_details
(
    id         UUID PRIMARY KEY     DEFAULT uuid_generate_v4(),
    gr         VARCHAR     NOT NULL,
    inflection VARCHAR,
    source_id  UUID        NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE definition_details
(
    id                    UUID PRIMARY KEY     DEFAULT uuid_generate_v4(),
    expression_details_id UUID        NOT NULL,
    language_id           VARCHAR     NOT NULL,
    dialect_id            INT         NOT NULL,
    created_at            TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at            TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE definition_details_tag
(
    tag_abbr              UUID        NOT NULL,
    definition_details_id UUID        NOT NULL,
    created_at            TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE definition
(
    id                    UUID PRIMARY KEY     DEFAULT uuid_generate_v4(),
    "value"               VARCHAR     NOT NULL,
    definition_details_id UUID        NOT NULL,
    created_at            TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at            TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE definition_tag
(
    tag_abbr      VARCHAR(10) NOT NULL,
    definition_id UUID        NOT NULL,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE tag
(
    abbreviation VARCHAR(10) PRIMARY KEY,
    description  VARCHAR,
    "type"       TAG_TYPE    NOT NULL,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at   TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE expression_example
(
    expression_details_id UUID        NOT NULL,
    example_id            UUID        NOT NULL,
    created_at            TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE definition_example
(
    example_id            UUID        NOT NULL,
    definition_details_id UUID        NOT NULL,
    created_at            TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE example
(
    id          UUID PRIMARY KEY     DEFAULT uuid_generate_v4(),
    source      VARCHAR,
    translation VARCHAR,
    -- source language id
    src_lang_id VARCHAR     NOT NULL,
    -- translation language id
    trl_lang_id VARCHAR,
    -- string combination of source and translation
    raw         VARCHAR     NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE language
(
    id         VARCHAR(3) PRIMARY KEY, -- iso639 alpha 3
    "name"     VARCHAR     NOT NULL UNIQUE,
    iso_2      VARCHAR(2) UNIQUE,      -- iso639 alpha 2
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE dialect
(
    id          SERIAL PRIMARY KEY,
    language_id VARCHAR     NOT NULL,
    "name"      VARCHAR     NOT NULL UNIQUE,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE mediafile
(
    id            UUID PRIMARY KEY,
    expression_id UUID        NOT NULL,
    mediatype     MEDIA_TYPE  NOT NULL,
    url           VARCHAR     NOT NULL,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE expression_relation
(
    expression_1_id  UUID        NOT NULL,
    expression_2_id  UUID        NOT NULL,
    relation_type_id INT         NOT NULL,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE relation_type
(
    id         SERIAL PRIMARY KEY,
    "name"     VARCHAR     NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE source
(
    id         UUID PRIMARY KEY     DEFAULT uuid_generate_v4(),
    "type"     SOURCE_TYPE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE written_source
(
    id               UUID PRIMARY KEY,
    source_id        UUID,
    "name"           VARCHAR,
    authors          VARCHAR,
    publication_year VARCHAR,
    provided_by      VARCHAR,
    provided_by_url  VARCHAR,
    processed_by     VARCHAR,
    copyright        VARCHAR,
    see_source_url   VARCHAR,
    -- Tells about how the source obtained its information when necessary
    description      VARCHAR,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at       TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE etymology
(
    id            UUID PRIMARY KEY     DEFAULT uuid_generate_v4(),
    expression_id UUID        NOT NULL,
    language_id   VARCHAR(3)  NOT NULL,
    dialect_id    INT,
    description   VARCHAR     NOT NULL,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE "case"
(
    id          UUID PRIMARY KEY,
    -- e.g. Nominative, Accusative etc.
    "name"      VARCHAR,
    -- question to be answered by given case e.g. Who/What?, Whom/What? etc.
    question    VARCHAR,
    -- refers to a language where given the case belongs to.
    language_id VARCHAR
);

CREATE TABLE declension
(
    id                    UUID PRIMARY KEY,
    -- expression to decline according to the given case | слово для склонения согласно указанному падежу
    expression_details_id UUID,
    case_id               UUID,
    "value"               VARCHAR,
    -- grammatical number e.g. single_1 for "доске" and plural_many for "досках"
    "num"                 GRAMM_NUMBER,
    person                GRAMM_PERSON,
    gender                GENDER
);
-- endregion TABLES

-- region ALTER TABLES
ALTER TABLE "expression"
    ADD FOREIGN KEY ("details_id") REFERENCES "expression_details" ("id"),
    ADD FOREIGN KEY ("language_id") REFERENCES "language" ("id");

ALTER TABLE "expression_details"
    ADD FOREIGN KEY ("source_id") REFERENCES "source" ("id");

ALTER TABLE "definition_details"
    ADD FOREIGN KEY ("expression_details_id") REFERENCES "expression_details" ("id"),
    ADD FOREIGN KEY ("language_id") REFERENCES "language" ("id"),
    ADD FOREIGN KEY ("dialect_id") REFERENCES "dialect" ("id");

ALTER TABLE "definition_details_tag"
    ADD FOREIGN KEY ("tag_abbr") REFERENCES "tag" ("abbreviation"),
    ADD FOREIGN KEY ("definition_details_id") REFERENCES "definition_details" ("id");

ALTER TABLE "definition"
    ADD FOREIGN KEY ("definition_details_id") REFERENCES "definition_details" ("id");

ALTER TABLE "definition_tag"
    ADD FOREIGN KEY ("tag_abbr") REFERENCES "tag" ("abbreviation"),
    ADD FOREIGN KEY ("definition_id") REFERENCES "definition" ("id");

ALTER TABLE "expression_example"
    ADD FOREIGN KEY ("expression_details_id") REFERENCES "expression_details" ("id"),
    ADD FOREIGN KEY ("example_id") REFERENCES "example" ("id");

ALTER TABLE "definition_example"
    ADD FOREIGN KEY ("example_id") REFERENCES "example" ("id"),
    ADD FOREIGN KEY ("definition_details_id") REFERENCES "definition_details" ("id");

ALTER TABLE "example"
    ADD FOREIGN KEY ("src_lang_id") REFERENCES "language" ("id"),
    ADD FOREIGN KEY ("trl_lang_id") REFERENCES "language" ("id");

ALTER TABLE "dialect"
    ADD FOREIGN KEY ("language_id") REFERENCES "language" ("id");

ALTER TABLE "mediafile"
    ADD FOREIGN KEY ("expression_id") REFERENCES "expression" ("id");

ALTER TABLE "expression_relation"
    ADD FOREIGN KEY ("expression_1_id") REFERENCES "expression" ("id"),
    ADD FOREIGN KEY ("expression_2_id") REFERENCES "expression" ("id"),
    ADD FOREIGN KEY ("relation_type_id") REFERENCES "relation_type" ("id");

ALTER TABLE "written_source"
    ADD FOREIGN KEY ("source_id") REFERENCES "source" ("id");

ALTER TABLE "etymology"
    ADD FOREIGN KEY ("expression_id") REFERENCES "expression" ("id"),
    ADD FOREIGN KEY ("language_id") REFERENCES "language" ("id"),
    ADD FOREIGN KEY ("dialect_id") REFERENCES "dialect" ("id");

ALTER TABLE "case"
    ADD FOREIGN KEY ("language_id") REFERENCES "language" ("id");

ALTER TABLE "declension"
    ADD FOREIGN KEY ("expression_details_id") REFERENCES "expression_details" ("id"),
    ADD FOREIGN KEY ("case_id") REFERENCES "case" ("id");
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
CREATE INDEX "created_at_index" ON "expression" ("created_at");

CREATE INDEX "updated_at_index" ON "expression" ("updated_at");

CREATE INDEX "created_at_index" ON "expression_details" ("created_at");

CREATE INDEX "updated_at_index" ON "expression_details" ("updated_at");

CREATE INDEX "created_at_index" ON "definition_details" ("created_at");

CREATE INDEX "updated_at_index" ON "definition_details" ("updated_at");

CREATE INDEX "created_at_index" ON "definition_details_tag" ("created_at");

CREATE INDEX "created_at_index" ON "definition" ("created_at");

CREATE INDEX "updated_at_index" ON "definition" ("updated_at");

CREATE INDEX "created_at_index" ON "definition_tag" ("created_at");

CREATE INDEX "created_at_index" ON "tag" ("created_at");

CREATE INDEX "updated_at_index" ON "tag" ("updated_at");

CREATE INDEX "created_at_index" ON "expression_example" ("created_at");

CREATE INDEX "created_at_index" ON "definition_example" ("created_at");

CREATE INDEX "created_at_index" ON "example" ("created_at");

CREATE INDEX "updated_at_index" ON "example" ("updated_at");

CREATE INDEX "created_at_index" ON "language" ("created_at");

CREATE INDEX "updated_at_index" ON "language" ("updated_at");

CREATE INDEX "created_at_index" ON "dialect" ("created_at");

CREATE INDEX "updated_at_index" ON "dialect" ("updated_at");

CREATE INDEX "created_at_index" ON "mediafile" ("created_at");

CREATE INDEX "updated_at_index" ON "mediafile" ("updated_at");

CREATE INDEX "created_at_index" ON "expression_relation" ("created_at");

CREATE INDEX "created_at_index" ON "relation_type" ("created_at");

CREATE INDEX "updated_at_index" ON "relation_type" ("updated_at");

CREATE INDEX "created_at_index" ON "source" ("created_at");

CREATE INDEX "updated_at_index" ON "source" ("updated_at");

CREATE INDEX "created_at_index" ON "written_source" ("created_at");

CREATE INDEX "updated_at_index" ON "written_source" ("updated_at");

CREATE INDEX "created_at_index" ON "etymology" ("created_at");

CREATE INDEX "updated_at_index" ON "etymology" ("updated_at");

CREATE INDEX exp_gin_idx ON expression USING gin (spelling gin_trgm_ops);
-- endregion

-- region Comments
COMMENT ON COLUMN "example"."src_lang_id" IS 'Source language ID';

COMMENT ON COLUMN "example"."trl_lang_id" IS 'Translation language ID';

COMMENT ON COLUMN "example"."raw" IS 'String combination of source and translation';

COMMENT ON COLUMN "language"."id" IS 'iso639_3';

COMMENT ON COLUMN "language"."iso_2" IS 'iso639_2';

COMMENT ON COLUMN "written_source"."description" IS 'Tells about how the source obtained its information when necessary';

COMMENT ON TABLE "case" IS 'Падеж';

COMMENT ON COLUMN "case"."name" IS 'e.g. Nominative, Accusative etc.';

COMMENT ON COLUMN "case"."question" IS 'question to be answered by given case e.g. Who/What?, Whom/What? etc.';

COMMENT ON COLUMN "case"."language_id" IS 'refers to a language where the given case belongs to';

COMMENT ON TABLE "declension" IS 'Склонение';

COMMENT ON COLUMN "declension"."expression_details_id" IS 'expression to decline according to the given case | слово для склонения согласно указанному падежу';

COMMENT ON COLUMN "declension"."num" IS 'grammatical number e.g. single_1 for "доске" and plural_many for "досках"';
-- endregion