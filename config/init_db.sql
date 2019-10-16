DROP TABLE IF EXISTS SECTION;
DROP TABLE IF EXISTS CONTACT;
DROP TABLE IF EXISTS RESUME CASCADE;
-- DROP INDEX IF EXISTS contact_uuid_type_index;
-- DROP SEQUENCE IF EXISTS contact_id_seq CASCADE ;

CREATE TABLE resume
(
    uuid      CHAR(36) PRIMARY KEY NOT NULL,
    full_name TEXT                 NOT NULL
);

CREATE TABLE contact
(
    id          SERIAL,
    resume_uuid CHAR(36) NOT NULL REFERENCES resume (uuid) ON DELETE CASCADE,
    type        TEXT     NOT NULL,
    value       TEXT     NOT NULL
);
CREATE UNIQUE INDEX contact_uuid_type_index
    ON contact (resume_uuid, type);

create table section
(
    id          SERIAL,
    resume_uuid CHAR(36) NOT NULL REFERENCES resume (uuid) ON DELETE CASCADE,
    type        TEXT     NOT NULL,
    value       TEXT     NOT NULL
);
CREATE UNIQUE INDEX section_uuid_type_index
    ON contact (resume_uuid, type);
