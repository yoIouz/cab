--liquibase formatted sql
--changeset Dmitry:2

create table rides
(
    id           bigserial
        constraint rides_pk
            primary key,
    driver_id    bigint  not null
        constraint rides_driver_id_fk
            references driver,
    passenger_id integer not null,
    distance     integer,
    inserted_at  timestamp default now()
);


