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
    tm DATETIME NOT NULL
                DEFAULT CURRENT_TIMESTAMP, 
    is_read varchar(1) not null
				DEFAULT 0
);

create table relations
(
    id_rel         int           not null
            primary key,
    id_f int not null references users(id),
    id_s int not null references users(id)
);

create unique index messages_id_uindex
    on messages(id);

