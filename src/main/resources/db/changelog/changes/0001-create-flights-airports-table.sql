--liquibase formatted sql

--changeset nikolajs:1

CREATE TABLE airports
(
    country VARCHAR(30) NOT NULL CHECK ( country <> '' ),
    city    VARCHAR(30) NOT NULL CHECK ( city <> '' ),
    airport VARCHAR(30) NOT NULL CHECK ( airport <> '' ) PRIMARY KEY UNIQUE
);

CREATE TABLE flights
(
    id             SERIAL UNIQUE PRIMARY KEY,
    airport_from   VARCHAR REFERENCES airports (airport),
    airport_to     VARCHAR REFERENCES airports (airport),
    carrier        VARCHAR NOT NULL CHECK ( carrier <> '' ),
    departure_time TIMESTAMP WITHOUT TIME ZONE        NOT NULL,
    arrival_time   TIMESTAMP WITHOUT TIME ZONE        NOT NULL,
    CHECK ( airport_from <> airport_to ),
    CHECK ( departure_time < arrival_time )
);

