INSERT IGNORE INTO role (id, name) VALUES (1, 'ADMIN');
INSERT IGNORE INTO role (id, name) VALUES (2, 'CUSTOMER');

INSERT IGNORE INTO customer (id, email, password) VALUES (1, 'admin', '$2a$10$k4EgiJBaVORtbQonGClsE.olMidT/cmIORYQfnKnLNFOSX.MrLqaW');
INSERT IGNORE INTO customer_role (customer_id, role_id) VALUES (1, 1);

INSERT IGNORE INTO customer (id, email, password) VALUES (2, 'customer2', '$2a$10$vJj8KXSpJd6SUZ52ueSZMuaCsVJUdHM3Nq.3171a4ItfWYV6Y9C42');
INSERT IGNORE INTO customer_role (customer_id, role_id) VALUES (2, 2);

INSERT IGNORE INTO customer (id, email, password) VALUES (3, 'customer3', '$2a$10$vJj8KXSpJd6SUZ52ueSZMuaCsVJUdHM3Nq.3171a4ItfWYV6Y9C42');
INSERT IGNORE INTO customer_role (customer_id, role_id) VALUES (3, 2);

INSERT IGNORE INTO customer (id, email, password) VALUES (4, 'customer4', '$2a$10$vJj8KXSpJd6SUZ52ueSZMuaCsVJUdHM3Nq.3171a4ItfWYV6Y9C42');
INSERT IGNORE INTO customer_role (customer_id, role_id) VALUES (4, 2);

INSERT IGNORE INTO customer (id, email, password) VALUES (5, 'customer5', '$2a$10$vJj8KXSpJd6SUZ52ueSZMuaCsVJUdHM3Nq.3171a4ItfWYV6Y9C42');
INSERT IGNORE INTO customer_role (customer_id, role_id) VALUES (5, 2);


INSERT IGNORE INTO asset (asset_name, size, usable_size, customer_id)
VALUES ('TRY', 1000.00, 1000.00, 2),
       ('ADA', 10.00, 10.00, 2),
       ('TRY', 1000.00, 900.00, 3),
       ('ADB', 10.00, 2.00, 3),
       ('TRY', 1000.00, 800.00, 4),
       ('ADC', 10.00, 6.00, 4),
       ('TRY', 1000.00, 700.00, 5),
       ('ADD', 5.00, 0.00, 5);


INSERT IGNORE INTO orders (asset_name, create_date, order_side, price, size, status, update_date, customer_id)
VALUES ('ADA', '2025-02-19 10:42:55', 'BUY', 10.00, 10.00, 'MATCHED', '2025-02-19 12:42:55', 2),

       ('ADB', '2025-02-19 11:42:55', 'BUY', 10.00, 10.00, 'MATCHED', '2025-02-19 12:42:50', 3),
       ('ADB', '2025-02-19 13:42:55', 'SELL', 20.00, 8.00, 'PENDING', '2025-02-19 13:42:55', 3),
       ('BDB', '2025-02-19 14:42:55', 'BUY', 20.00, 5.00, 'PENDING', '2025-02-19 14:42:55', 3),

       ('ADC', '2025-02-19 11:42:55', 'BUY', 10.00, 10.00, 'MATCHED', '2025-02-19 12:42:50', 4),
       ('ADC', '2025-02-19 13:42:55', 'SELL', 20.00, 2.00, 'PENDING', '2025-02-19 13:42:55', 4),
       ('ADC', '2025-02-19 13:42:55', 'SELL', 25.00, 2.00, 'PENDING', '2025-02-19 13:42:55', 4),
       ('CDC', '2025-02-19 14:42:55', 'BUY', 20.00, 10.00, 'PENDING', '2025-02-19 14:42:55', 4),

       ('ADD', '2025-02-18 11:42:55', 'BUY', 50.00, 5.00, 'MATCHED', '2025-02-18 12:42:50', 5),
       ('ADD', '2025-02-18 13:42:55', 'SELL', 70.00, 5.00, 'PENDING', '2025-02-18 13:42:55', 5),
       ('CDD', '2025-02-18 14:42:55', 'BUY', 30.00, 10.00, 'PENDING', '2025-02-18 14:42:55', 5);
