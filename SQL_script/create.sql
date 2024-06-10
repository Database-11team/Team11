CREATE DATABASE IF NOT EXISTS DB2024Team11;
USE DB2024Team11;

CREATE TABLE IF NOT EXISTS DB2024_RESTAURANT (
	restaurant_id INTEGER AUTO_INCREMENT,
	restaurant_name VARCHAR(20) NOT NULL,
	restaurant_category VARCHAR(10),
	location VARCHAR(50) NOT NULL,
	contact VARCHAR(40),
	opening_hours VARCHAR(50),
	breaktime VARCHAR(50),
	lastorder VARCHAR(50),
	closed_day VARCHAR(50),
	table_num INTEGER,
	availability TINYINT NOT NULL,
	PRIMARY KEY(restaurant_id)
 );

CREATE TABLE IF NOT EXISTS DB2024_CUSTOMER(
	customer_id INTEGER AUTO_INCREMENT,
	customer_name VARCHAR(20) NOT NULL,
	birthday DATE,
	phone_number VARCHAR(40) NOT NULL,
	PRIMARY KEY(customer_id)
);

CREATE TABLE IF NOT EXISTS DB2024_MENU (
	menu_id INTEGER AUTO_INCREMENT,
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

CREATE TABLE IF NOT EXISTS DB2024_RESERVATION (
	reservation_id INT AUTO_INCREMENT,
	customer_id INT,
	restaurant_id INT,
	reservation_date DATE NOT NULL,
	reservation_time TIME NOT NULL,
	reservation_guests INT NOT NULL,
	reservation_confirmed TINYINT NOT NULL,
	PRIMARY KEY(reservation_id),
	FOREIGN KEY(customer_id) REFERENCES DB2024_CUSTOMER(customer_id) ON DELETE CASCADE,
	FOREIGN KEY(restaurant_id) REFERENCES DB2024_RESTAURANT(restaurant_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS DB2024_ORDER (
	order_id INT AUTO_INCREMENT,
	menu_id INT,
	restaurant_id INT,
	reservation_id INT,
	order_time DATETIME NOT NULL,
	PRIMARY KEY(order_id),
	FOREIGN KEY(menu_id) REFERENCES DB2024_MENU(menu_id) ON DELETE SET NULL,
	FOREIGN KEY(restaurant_id) REFERENCES DB2024_RESTAURANT(restaurant_id) ON DELETE CASCADE,
	FOREIGN KEY(reservation_id) REFERENCES DB2024_RESERVATION(reservation_id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS DB2024_PAYMENT (
    payment_id INTEGER AUTO_INCREMENT,
    reservation_id INT,
    payment_date DATETIME NOT NULL,
    payment_type VARCHAR(10) NOT NULL,
    payment_amount INTEGER NOT NULL,
    cash_receipt_requested TINYINT(1),
	  PRIMARY KEY(payment_id),
	  FOREIGN KEY(reservation_id) REFERENCES DB2024_RESERVATION(reservation_id) ON DELETE CASCADE
);