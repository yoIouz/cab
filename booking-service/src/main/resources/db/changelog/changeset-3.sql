--liquibase formatted sql
--changeset Dmitry:3

alter table ride_requests
    rename to ride;

alter table ride
    add status text;

alter table ride
    add column driver_id bigint;

