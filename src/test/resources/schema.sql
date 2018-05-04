/* 
 * Copyright (C) 2018 Spyros Palaiokostas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * Author:  Spyros Palaiokostas
 * Created: Apr 27, 2018
 */


DROP TABLE IF EXISTS books;

CREATE TABLE books (
  id int(11) NOT NULL,
  title varchar(45) NOT NULL,
  author varchar(45) NOT NULL,
  publisher varchar(45) DEFAULT NULL,
  publication_year int(11) DEFAULT NULL,
  description varchar(255) DEFAULT NULL,
  price_cents int(11),
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id int(11) NOT NULL,
  username varchar(45) NOT NULL,
  email varchar(64) NOT NULL,
  password varchar(255) NOT NULL,
  roles varchar(45),
  PRIMARY KEY (id),
  UNIQUE KEY username_UNIQUE (username),
  UNIQUE KEY email_UNIQUE (email)
);


DROP TABLE IF EXISTS orders;

CREATE TABLE orders (
  id int(11) NOT NULL,
  user_id int(11) DEFAULT NULL,
  book_id int(11) DEFAULT NULL,
  quantity int(11) DEFAULT NULL,
  submission_date TIMESTAMP DEFAULT NULL,
  status varchar(45) DEFAULT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

