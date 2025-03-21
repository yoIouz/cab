create table public.transactions
(
    id               bigserial      not null,
    user_id          bigint         not null,
    amount           numeric(10, 2) not null,
    transaction_date timestamp      not null,
    primary key (id, transaction_date)
);

insert into public.transactions(id, user_id, amount, transaction_date)
VALUES (1, 1, -25.50, now()),
       (2, 1, -31.30, now());


