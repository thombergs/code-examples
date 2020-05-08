create table hello_user (
    id varchar(36) not null unique,
    name varchar(100) not null,
    primary key(id)
);