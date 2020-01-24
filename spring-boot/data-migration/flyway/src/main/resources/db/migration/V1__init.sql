-- Init script

-- DDL
CREATE TABLE auth_user(
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL unique
);