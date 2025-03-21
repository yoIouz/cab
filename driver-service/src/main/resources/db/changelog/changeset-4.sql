--liquibase formatted sql
--changeset Dmitry:4

alter table driver add column total_rating_count integer default 0;