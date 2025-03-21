--liquibase formatted sql
--changeset Dmitry:1

create table driver
(
    id            bigserial
        constraint driver_pk
            primary key,
    name          varchar(32),
    car           varchar(32),
    client_rating float,
    inserted_at   timestamp default now()
);

