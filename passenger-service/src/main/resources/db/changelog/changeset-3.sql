--liquibase formatted sql
--changeset Dmitry:3

create table rides
(
    id           bigserial
        constraint rides_pk
            primary key,
    passenger_id    bigint  not null
        constraint rides_driver_id_fk
            references passenger,
    driver_id bigint not null,
    status     text,
    inserted_at  timestamp default now()
);





