create table public.driver
(
    id                 bigint not null
        constraint driver_pk
            primary key,
    name               varchar(32),
    car                varchar(32),
    rating             double precision default 0.0,
    inserted_at        timestamp        default now(),
    total_rating_count integer          default 0
);

insert into driver(id, name, car)
VALUES (1, 'Ахмед', '7839AT-7');

create table public.driver_status
(
    id        bigserial
        constraint driver_status_pk
            primary key,
    driver_id bigint
        constraint driver_status_driver_id_fk
            references public.driver,
    is_busy   boolean default false not null
);

insert into driver_status(driver_id, is_busy)
VALUES (1, false);


