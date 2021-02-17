create table post
(
    id          serial primary key,
    name        varchar(250) not null,
    description text not null,
    link        varchar(250) not null unique,
    created     timestamp
)