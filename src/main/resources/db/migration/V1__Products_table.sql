CREATE TABLE product (
    id int4 PRIMARY KEY,
    price double precision,
    name text
);

INSERT INTO product VALUES (1, 2, 'product A');
INSERT INTO product VALUES (2, 3, 'product B');
INSERT INTO product VALUES (3, 4, 'product C');