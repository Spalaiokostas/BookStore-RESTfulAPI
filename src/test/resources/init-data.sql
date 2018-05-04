

TRUNCATE TABLE books;
INSERT INTO books (id, title, author, publisher, publication_year, description, price_cents) VALUES 
(1,'title','author','publisher',2018,'description',10000);

TRUNCATE TABLE users;
INSERT INTO users (id, username, email, password, roles) VALUES 
(1,'username','email@provider.domain','$2a$10$kyJx6UnObbOuKzvK7ICe..YPFPgH.AhoNnaANisVFPdiiuxGr9LEi','ROLE_USER');

TRUNCATE TABLE orders;
INSERT INTO orders (id, user_id, book_id, quantity, submission_date, status)
VALUES (1,1,1,1,'2018-01-19 01:14:00','PENDING');
