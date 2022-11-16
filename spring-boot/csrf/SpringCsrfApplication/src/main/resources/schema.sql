TRUNCATE SCHEMA PUBLIC AND COMMIT;

CREATE TABLE IF NOT EXISTS TOKEN( ID INTEGER  GENERATED BY DEFAULT AS IDENTITY (START WITH 1000), USER VARCHAR(45) NOT NULL, TOKEN VARCHAR(45) NOT NULL, PRIMARY KEY(ID));

CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START WITH 1 INCREMENT BY 1;