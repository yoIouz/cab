--liquibase formatted sql
--changeset Dmitry:2

alter table passenger
    add column total_rating_count integer default 0;
alter table passenger
    add column inserted_at timestamp default now();
