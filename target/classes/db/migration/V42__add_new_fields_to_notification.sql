ALTER TABLE notification
    ADD COLUMN title VARCHAR(255);

ALTER TABLE notification
    ADD COLUMN image_id BIGINT;

ALTER TABLE notification
    ADD COLUMN arrived_date VARCHAR(255);
