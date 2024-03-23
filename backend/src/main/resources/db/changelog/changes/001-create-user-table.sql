CREATE TABLE app_user
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(50) NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE
);