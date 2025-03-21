--liquibase formatted sql
--changeset Dmitry:5

alter table driver
    rename column client_rating to rating;

alter table driver
    alter column rating set default 0.0;

