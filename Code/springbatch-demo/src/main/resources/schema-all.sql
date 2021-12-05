DROP TABLE if exists people;
CREATE TABLE  IF NOT EXISTS people
(
    person_id bigserial primary key,
    first_name VARCHAR(20),
    last_name  VARCHAR(20)
);