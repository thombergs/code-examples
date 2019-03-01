create table booking (
   id bigint,
   customer_id bigint,
   flight_number varchar
);

create table customer (
   id bigint,
   name varchar
);

create table flight (
   flight_number varchar,
   airline varchar
);

create table user (
   id bigint,
   name varchar,
   email varchar,
   registration_date timestamp
);

create sequence hibernate_sequence;