create table CUSTOMER (
  id NUMBER,
  name VARCHAR
);

create table ADDRESS (
  id NUMBER,
  customer_id NUMBER,
  street VARCHAR,
  FOREIGN KEY (customer_id) REFERENCES CUSTOMER(id)
);
