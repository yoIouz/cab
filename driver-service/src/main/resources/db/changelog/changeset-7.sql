--liquibase formatted sql
--changeset Dmitry:7

alter table driver
    alter column id drop default;

drop sequence driver_id_seq;




