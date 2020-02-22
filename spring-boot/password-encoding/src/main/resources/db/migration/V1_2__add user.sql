insert into users (username, password, enabled)
VALUES ('admin', '{bcrypt}$2a$10$4V9kA793Pi2xf94dYFgKWuw8ukyETxWb7tZ4/mfco9sWkwvBQndxW', true);
insert into users (username, password, enabled)
VALUES ('user',
        '{SHA-256}{4Cc0+yDMHnTUy+zOHeMH7yaPhxvlJT//tQTwEhyegiQ=}446d06130bfc254527a7bbd95b50595a977c0058110f8dccb54bd273d99325b8',
        true);
insert into users (username, password, enabled)
VALUES ('user with working factor 5', '{bcrypt}$2a$05$Zz4rToG8YXKMbuAPgm3qj.HpTFsGEdZHhCf9ikIHAoI5elX7ajNm.', true);
insert into users (username, password, enabled)
VALUES ('user with sha1 encoding',
        '{SHA-1}{6tND0AZfFH3aE1VDg7QkWT6DzFg/NUHtukntgwu8JV4=}804c6e8efebf4e91f88e3baf9fd383e28a21378c', true);
insert into users (username, password, enabled)
VALUES ('scrypt user',
        '{scrypt}$e0801$fUx3MxN07zdH3UyARJqOwv3WiWCvE7f6qRm9A5KQfNo5ovSwxMHknQ4vERO4csj/I3imG2HJQg1HHp7Rqzbp7g==$Fm5F9PSoE/jBYLOmnCJcvX1Euf952r5b3BjAl+SwQMs=',
        true);