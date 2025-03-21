--liquibase formatted sql
--changeset Dmitry:1

create table invoice
(
    id        bigserial
        constraint invoice_pk
            primary key,
    balance   money,
    client_id bigint not null
);

