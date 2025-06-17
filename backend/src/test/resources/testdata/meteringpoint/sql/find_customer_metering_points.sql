INSERT INTO customers (id, first_name, last_name, username, password_hash)
VALUES ('adfea66d-ef31-434e-b618-c6e316f293c0', 'Test', 'Customer', 'testcustomer', '$2a$10$ga2F.Dv4sRhvxrq.90gYkuil/qZHl1RoO4SdfDdbgwNSG.lpvXwLa'),
       ('acfe16cc-aa61-48fd-8644-34de31ad9433', 'Jane', 'Doe', 'janedoe', '$2a$10$su1vFDI/iuXWJPaVfp2pwOKHapp7aATy0UJHVSRitTGIEq.VKPgGS');

INSERT INTO metering_point (id, customer_id, address)
VALUES ('7d4e5f99-8d2d-4637-8a3c-55e59c3af734', 'adfea66d-ef31-434e-b618-c6e316f293c0', 'Sample address 1'),
       ('ab2edd68-acf0-4419-b1ea-36afb17d0f9a', 'adfea66d-ef31-434e-b618-c6e316f293c0', 'Other address 2');