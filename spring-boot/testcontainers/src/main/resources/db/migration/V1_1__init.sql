CREATE TABLE car
(
    id    uuid PRIMARY KEY,
    registration_number VARCHAR(255),
    name  varchar(64) NOT NULL,
    color varchar(32) NOT NULL,
    registration_timestamp INTEGER
);