DELETE FROM meals;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (user_id, description, date_time, calories)
VALUES (100000, 'user breakfast', '15-01-1988 04:05', 1200),
       (100000, 'user-dinner', '15-01-1988 13:05', 1100),
       (100000, 'user-supper', '15-01-1988 19:59', 900),
       (100001, 'admin breakfast', '15-01-1988 08:00', 1200),
       (100001, 'admin-dinner', '15-01-1988 12:27', 1100),
       (100001, 'admin-supper', '15-01-1988 22:03', 900);


