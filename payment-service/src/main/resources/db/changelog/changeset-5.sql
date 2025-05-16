--liquibase formatted sql
--changeset Dmitry:5

alter table public.transactions
drop constraint fk_transactions_user;

alter table public.transactions
    add constraint fk_transactions_user
        foreign key (user_id) references user_balance (user_id);
