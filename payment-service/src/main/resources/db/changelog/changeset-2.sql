--liquibase formatted sql
--changeset Dmitry:2

drop table if exists invoice;

create table user_balance
(
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT         NOT NULL,
    balance DECIMAL(10, 2) NOT NULL
);

CREATE TABLE transactions
(
    id               BIGSERIAL,
    user_id          BIGINT         NOT NULL,
    amount           DECIMAL(10, 2) NOT NULL,
    transaction_date TIMESTAMP      NOT NULL,
    primary key (id, transaction_date)
) PARTITION BY RANGE (transaction_date);

ALTER TABLE transactions
    ADD CONSTRAINT fk_transactions_user FOREIGN KEY (user_id) REFERENCES user_balance(id);


