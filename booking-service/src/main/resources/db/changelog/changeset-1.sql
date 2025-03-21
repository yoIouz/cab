--liquibase formatted sql
--changeset Dmitry:1

create table ride_requests
(
    id            bigserial
        constraint ride_requests_pk
            primary key,
    passenger_id  bigint,
    initial_point text,
    destination   text,
    distance      int,
    inserted_at   timestamp default now()
);