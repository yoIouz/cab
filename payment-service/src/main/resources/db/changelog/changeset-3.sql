--liquibase formatted sql
--changeset Dmitry:3

create index transactions_user_id_index
    on public.transactions (user_id);



