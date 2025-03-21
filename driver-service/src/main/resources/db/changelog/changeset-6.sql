--liquibase formatted sql
--changeset Dmitry:6

alter table rides
    drop column distance;

alter table rides
    add column status text;