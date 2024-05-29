CREATE DATABASE DB2024Team11;
USE DB2024Team11;

CREATE TABLE DB2024_RESTAURANT (
	restaurant_id INTEGER,
	restaurant_name VARCHAR(20) NOT NULL,
	restaurant_category VARCHAR(10),
	location VARCHAR(20) NOT NULL,
	contact VARCHAR(40),
	opening_hours VARCHAR(20),
	breaktime VARCHAR(20),
	lastorder VARCHAR(10),
	closed_day VARCHAR(20),
	table_num INTEGER,
	availability TINYINT NOT NULL,
	PRIMARY KEY(restaurant_id)
    );

CREATE TABLE DB2024_CUSTOMER(
	customer_id INTEGER,
	customer_name VARCHAR(20) NOT NULL,
	birthday DATE,
	phone_number VARCHAR(40) NOT NULL,
	PRIMARY KEY(customer_id)
);

CREATE TABLE DB2024_MENU (
	menu_id INTEGER,
	restaurant_id INTEGER,
	menu_category VARCHAR(20),
	menu_name VARCHAR(30) NOT NULL,
	price INTEGER NOT NULL,
	allergic_included VARCHAR(100),
	stock INT NOT NULL,
	description VARCHAR(100),
	PRIMARY KEY(menu_id),
	FOREIGN KEY(restaurant_id) REFERENCES DB2024_RESTAURANT(restaurant_id) ON DELETE CASCADE
);

CREATE TABLE DB2024_TABLE (
	table_id INT,
	restaurant_id INT,
	capacity INT NOT NULL,
	meal_duration BIGINT,
	payment_status VARCHAR(20) NOT NULL,
	PRIMARY KEY(table_id),
	FOREIGN KEY (restaurant_id) REFERENCES DB2024_RESTAURANT(restaurant_id) ON DELETE CASCADE
);

CREATE TABLE DB2024_ORDER (
	order_id INT,
	customer_id INT,
	menu_id INT,
	restaurant_id INT,
	table_id INT,
	order_time DATETIME NOT NULL,
	PRIMARY KEY(order_id),
	FOREIGN KEY(customer_id) REFERENCES DB2024_CUSTOMER(customer_id) ON DELETE SET NULL,
	FOREIGN KEY(menu_id) REFERENCES DB2024_MENU(menu_id) ON DELETE SET NULL,
	FOREIGN KEY(restaurant_id) REFERENCES DB2024_RESTAURANT(restaurant_id) ON DELETE CASCADE,
	FOREIGN KEY(table_id) REFERENCES DB2024_TABLE(table_id) ON DELETE SET NULL
);

CREATE TABLE DB2024_RESERVATION (
	reservation_id INT,
	customer_id INT,
	restaurant_id INT,
	table_id INT,
	reservation_date DATE NOT NULL,
	reservation_time TIME NOT NULL,
	reservation_guests INT NOT NULL,
	reservation_confirmed TINYINT NOT NULL,
	PRIMARY KEY(reservation_id),
	FOREIGN KEY(customer_id) REFERENCES DB2024_CUSTOMER(customer_id) ON DELETE CASCADE,
	FOREIGN KEY(restaurant_id) REFERENCES DB2024_RESTAURANT(restaurant_id) ON DELETE SET NULL,
	FOREIGN KEY(table_id) REFERENCES DB2024_TABLE(table_id) ON DELETE SET NULL
);

CREATE TABLE DB2024_PAYMENT (
    payment_id INTEGER,
    payment_date DATETIME NOT NULL,
    payment_type VARCHAR(10) NOT NULL,
    payment_amount INTEGER NOT NULL,
    cash_receipt_requested TINYINT(1),
	  order_id INTEGER,
	  table_id INTEGER,
	  PRIMARY KEY(payment_id),
	  FOREIGN KEY(order_id) REFERENCES DB2024_ORDER(order_id) ON DELETE SET NULL,
	  FOREIGN KEY(table_id) REFERENCES DB2024_TABLE(table_id) ON DELETE SET NULL
);