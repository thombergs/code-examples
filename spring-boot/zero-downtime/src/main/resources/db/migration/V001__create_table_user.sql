create table customer (
    id serial unique,
    first_name varchar(100),
    last_name varchar(100),
    address varchar(255),
    primary key (id)
);
