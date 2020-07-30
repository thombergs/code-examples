CREATE TABLE car
(
    id    UUID PRIMARY KEY,
    name  VARCHAR(512),
    color VARCHAR(512),
    correlation_id UUID
);

CREATE TABLE registration
(
    id        UUID PRIMARY KEY,
    date      DATE,
    owner     VARCHAR(512),
    signature VARCHAR(1024),
    car_id    UUID
)