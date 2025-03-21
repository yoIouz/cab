--liquibase formatted sql
--changeset Dmitry:8

alter table driver
    drop column is_busy;

create table driver_status
(
    id        bigserial
        constraint driver_status_pk
            primary key,
    driver_id bigint
        constraint driver_status_driver_id_fk
            references driver,
    is_busy   boolean default false not null
);


