create table users (
    id integer primary key,
    nickname varchar(100) not null,
    password varchar(100) not null
);

create table messages
(
    id         int           not null
        constraint messages_pk
            primary key,
    text       varchar(1000) not null,
    fUser int           not null,
    tUser   int           not null,
    tm         timestamp     not null
);

create unique index messages_id_uindex
    on messages(id);

