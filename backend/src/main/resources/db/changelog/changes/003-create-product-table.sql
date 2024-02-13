CREATE TABLE products
(
    id            SERIAL PRIMARY KEY,
    entry_date    DATE,
    item_code     VARCHAR(255),
    item_name     VARCHAR(255),
    item_quantity INT,
    status        VARCHAR(255)
);
