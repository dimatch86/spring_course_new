
insert into books_schema_test.category (id, name, create_at, update_at) values (1, 'hunting', '2023-11-25 14:00:47.051349 +00:00', '2023-11-25 14:00:47.051349 +00:00');

insert into books_schema_test.book (id, title, author, create_at, update_at, category_id) values (1, 'Book1', 'Pushkin', '2023-11-25 14:00:47.051349 +00:00', '2023-11-25 14:00:47.051349 +00:00', 1);
insert into books_schema_test.book (id, title, author, create_at, update_at, category_id) values (2, 'Book2', 'Gogol', '2023-11-25 14:00:47.051349 +00:00', '2023-11-25 14:00:47.051349 +00:00', 1);


SELECT SETVAL((SELECT PG_GET_SERIAL_SEQUENCE('"category"', 'id')), (SELECT (MAX("id") + 1) FROM "category"), FALSE);
SELECT SETVAL((SELECT PG_GET_SERIAL_SEQUENCE('"book"', 'id')), (SELECT (MAX("id") + 1) FROM "book"), FALSE);