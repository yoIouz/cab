--liquibase formatted sql
--changeset Dmitry:4

alter table ride
    add price decimal(10,2) not null default 0.0;

alter table ride
    add completed_at timestamp;

