TRUNCATE SCHEMA PUBLIC AND COMMIT;

CREATE TABLE IF NOT EXISTS USER( ID INTEGER , FIRST_NAME VARCHAR(45) NOT NULL, LAST_NAME VARCHAR(45) NOT NULL, CITY VARCHAR(45), CREATED_DATE DATE, PRIMARY KEY(ID));

CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START WITH 1 INCREMENT BY 1;
