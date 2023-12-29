CREATE TABLE registration
(
    id       INT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);
INSERT INTO registration (id, username, password)
VALUES (1, 'john', '123');
INSERT INTO registration (id, username, password)
VALUES (2, 'jane', '123');