CREATE TABLE list_support_table
(
    id                  BIGSERIAL PRIMARY KEY,
    person_name         VARCHAR(255) NOT NULL,
    person_email        VARCHAR(255) NOT NULL,
    person_phone_number INTEGER,
    message             TEXT,
    date_sent           VARCHAR(255)
);