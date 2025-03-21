--liquibase formatted sql
--changeset Dmitry:3

alter table driver add column is_busy boolean default false;