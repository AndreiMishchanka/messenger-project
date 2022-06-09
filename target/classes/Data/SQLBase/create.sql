drop table users;
drop table messages;
drop table relations;
drop table groups;
drop table group_users;
drop table group_messages;

create table users (
    id integer primary key,
    nickname varchar(100) not null,
    password varchar(100) not null
);

create table messages(
    id         int           not null,
    text       varchar(1000) not null,
    fUser int           not null,
    tUser   int           not null,
    tm timestamp NOT NULL
                DEFAULT CURRENT_TIMESTAMP, 
    is_read varchar(1) not null
				DEFAULT 0,
                constraint messages_pk
            primary key(id)
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


create table groups
(
    id          int          not null
        constraint groups_pk
            primary key,
    name        varchar(100) not null,
    description varchar(100)
);

create unique index groups_id_uindex
    on groups (id);

create table group_users
(
    group_id int not null,
    user_id  int not null
);

create table group_messages
(
    group_id     int not null,
    from_id      int not null,
    message_text varchar(1000),
    tm timestamp NOT NULL
                DEFAULT CURRENT_TIMESTAMP
);