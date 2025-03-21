--liquibase formatted sql
--changeset Dmitry:2

drop table ride_requests;

create table ride_requests
(
    id               bigserial
        constraint ride_requests_pk
            primary key,
    passenger_id     bigint,
    initial_long     double precision,
    initial_lat      double precision,
    destination_long double precision,
    destination_lat  double precision,
    distance         double precision,
    inserted_at      timestamp default now()
);