-- auto-generated definition
create table users
(
    id               bigserial
        constraint users_pk
            primary key,
    firstname        varchar(20),
    second_name      varchar(20),
    age              integer,
    telephone_number varchar(20),
    email            varchar(20),
    created          timestamp default now() not null,
    updated          timestamp default now() not null,
    sex              varchar(1),
    is_deleted       boolean   default false
);

alter table users
    owner to katusha;

-- auto-generated definition
create table flyway_schema_history
(
    installed_rank integer                 not null
        constraint flyway_schema_history_pk
            primary key,
    version        varchar(50),
    description    varchar(200)            not null,
    type           varchar(20)             not null,
    script         varchar(1000)           not null,
    checksum       integer,
    installed_by   varchar(100)            not null,
    installed_on   timestamp default now() not null,
    execution_time integer                 not null,
    success        boolean                 not null
);

alter table flyway_schema_history
    owner to katusha;

create index flyway_schema_history_s_idx
    on flyway_schema_history (success);

-- auto-generated definition
create table l_users_product
(
    id         integer not null
        constraint l_users_product_pk
            primary key,
    user_id    bigint  not null
        constraint l_users_product_users_id_fk
            references users
            on update cascade on delete cascade,
    product_id bigint  not null
        constraint l_users_product_product_id_fk
            references product
            on update cascade on delete cascade,
    created    timestamp default now(),
    updated    timestamp default now()
);

alter table l_users_product
    owner to katusha;

-- auto-generated definition
create table product
(
    id      bigserial
        constraint product_pk
            primary key,
    name    varchar(50)      not null,
    price   double precision not null,
    created timestamp default now(),
    updated timestamp default now()
);

alter table product
    owner to katusha;

-- auto-generated definition
create table records
(
    id         serial
        primary key,
    full_name  varchar(255) not null,
    birth_date date         not null,
    team       varchar(255) not null,
    home_city  varchar(255) not null,
    squad      varchar(255) not null,
    position   varchar(50)  not null
);

alter table records
    owner to katusha;

-- auto-generated definition
create table security
(
    id       integer     default nextval('security_id_seq'::regclass) not null
        constraint security_pk
            primary key,
    login    varchar(20)                                              not null,
    password varchar(20)                                              not null,
    role     varchar(20) default 'USER'::character varying            not null,
    created  timestamp   default now()                                not null,
    updated  timestamp   default now(),
    user_id  bigint                                                   not null
        unique
        constraint security_users_id_fk
            references users
            on update cascade on delete cascade
);

alter table security
    owner to katusha;
