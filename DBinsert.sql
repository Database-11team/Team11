// RESTUARANT TABLE 삽입
INSERT INTO DB2024_RESTAURANT (
    restaurant_id, restaurant_name, restaurant_category, location, opening_hours, breaktime, lastorder, closed_day, table_num, availability, contact
) VALUES
(1, '모미지식당', '일식', '서대문구 이화여대7길 24', '월~금 11:00 ~ 20:30 토11:30 ~ 20:30', '15:00 ~ 17:00', '20:00', '일요일', 10, 0, '070-4154-2000'),
(2, '까이식당', '동남아음식', '서대문구 이화여대2가길 24', '월~금 11:00 ~ 20:00 토 11:00 ~ 15:00', '15:00 ~ 17:00', NULL, '일요일, 공휴일', 5, 0, '070-7779-8899'),
(3, '티엔미미', '중식', '마포구 양화로 144', '11:00 ~ 21:00', '15:00 ~ 17:00', NULL, NULL, 1, 1, '010-8823-1070'),
(4, '을밀대', NULL, '마포구 숭문길 24', '11:00 ~ 22:00', NULL, NULL, NULL, 12, 1, '02-717-1922'),
(5, '오제제', '일식', '서울 중구 세종대로 136', '11:00 ~ 21:00', '15:00 ~ 17:30', '14:30, 20:00', NULL, 12, 0, NULL),
(6, '을지다락', '양식', '서울 중구 수표로10길 19', '월~금 11:00 ~ 20:20 토,일 11:00 ~ 20:00', NULL, NULL, NULL, 10, 1, '070-8844-4484'),
(7, '칸다소바', '일식', '서울 종로구 자하문로7길 5', '11:30 ~ 21:00', '15:00 ~ 17:00', '14:30, 20:30', NULL, 8, 1, NULL),
(8, '노티드', '디저트', '서울 종로구 북촌로 6-3', '10:00 ~ 21:00', NULL, NULL, NULL, 6, 1, '070-7755-9377'),
(9, '쌤쌤쌤', '양식', '서울 용산구 한강대로50길 25', '11:30 ~ 21:50', '15:00 ~ 17:30', '14:30, 21:00', NULL, 7, 0, '02-797-1077'),
(10, '소문난성수감자탕', NULL, '서울 성동구 연무장길 45', '00:00 ~ 24:00', NULL, NULL, NULL, 15, 0, '02-465-6580');

//CUSTOMER TABLE 삽입
INSERT INTO DB2024_CUSTOMER (customer_id, customer_name, birthday, phone_number) VALUES
(1, '김민준', '1990-02-15', '010-1234-5678'),
(2, '이서연', NULL, '010-2345-6789'),
(3, '박지훈', '1995-11-22', '010-3456-7890'),
(4, '최수진', '1993-04-05', '010-4567-8901'),
(5, '정예은', '1992-07-30', '010-5678-9012'),
(6, '강민수', NULL, '010-6789-0123'),
(7, '윤지아', '1998-12-19', '010-7890-1234'),
(8, '장우진', '1991-03-27', '010-8901-2345'),
(9, '홍서윤', '1989-05-08', '010-9012-3456'),
(10, '서준혁', '1996-08-17', '010-0123-4567');

//MENU TABLE 삽입
INSERT INTO DB2024_MENU (menu_id, restaurant_id, menu_category, menu_name, price, allergic_included, stock, description) VALUES
(1, 1, NULL, '육회덮밥', 9500, NULL, 78, '양념된 밥 위에 육회가 한가득'),
(2, 1, NULL, '소고기가지덮밥', 11000, NULL, 62, '가지스아게를 소고기와 양념에 볶아 밥 위에 올린 덮밥'),
(3, 2, NULL, '치킨라이스', 10000, NULL, 21, '부드러운 닭고기와 닭 육수로 지은 고슬한 밥'),
(4, 3, '식사류', '토마토탕면', 15000, '땅콩', 58, '고운 빛의 붉은 향이 일어나는 매콤 달콤 토마토탕면'),
(5, 3, '식사류', '해물 황금 볶음밥', 15000, '갑각류', 62, '황금빛이 나는 중국식 볶음밥'),
(6, 3, '딤섬류', '소룡포', 8000, NULL, NULL, '육즙이 가득한 중국식 교자'),
(7, 4, NULL, '물냉면', 15000, NULL, 24, NULL),
(8, 5, '카츠류', '안심돈까츠', 17000, NULL, 46, NULL),
(9, 5, '카츠류', '등심돈까츠', 16000, NULL, 32, NULL),
(10, 5, '우동류', '자루우동', 11000, NULL, 48, NULL),
(11, 6, '메인', '다락오므라이스', 16000, NULL, 56, '유럽식 스타일의 볶은 밥, 부드러운 일본식 오믈렛.'),
(12, 6, '메인', '게살매콤크림리조또', 18000, '갑각류', 49, '고추기름으로 볶아낸 야채와 신선한 게살 그리고 크림소스'),
(13, 6, '사이드', '가츠산도', 12000, NULL, 61, '육즙 가득한 로스카츠로 만든 일본식 샌드위치'),
(14, 7, NULL, '마제소바', 11000, NULL, 31, '일본에서 화제인 국물 없이 비벼먹는 라멘'),
(15, 7, NULL, '아부라소바', 11000, NULL, 53, NULL),
(16, 8, '베이커리', '우유생크림도넛', 3900, NULL, 38, NULL),
(17, 8, '베이커리', '클래식 바닐라 도넛', 3500, NULL, 54, NULL),
(18, 8, '베이커리', '스트로베리 크림도넛', 4200, NULL, 72, NULL),
(19, 8, '음료', '아메리카노', 4500, NULL, 82, NULL),
(20, 8, '음료', '카페라떼', 5500, NULL, 102, NULL),
(21, 9, '에피타이저', '고구마 뇨끼', 9000, NULL, 79, '고구마 크림 베이스 소스의 단호박 뇨끼'),
(22, 9, '메인', '문어 리조또', 21000, NULL, 37, '고소한 풍미의 먹물 리조또'),
(23, 9, '메인', '잠봉뵈르 파스타', 19000, NULL, 62, '잠봉으로 만든 버터 풍미 가득 파스타'),
(24, 10, NULL, '감자탕(대)', 46000, NULL, 32, NULL),
(25, 10, NULL, '감자탕(중)', 34000, NULL, 29, NULL)

//ORDER TABLE 삽입
INSERT INTO DB2024_ORDER (order_id, customer_id, menu_id, restaurant_id, reservation_id, order_time) VALUES
(1, 4, 1, 1, 1, '2024-05-15 17:45:52'),
(2, 4, 2, 1, 1, '2024-05-15 17:45:52'),
(3, 2, 3, 2, 2, '2024-05-19 10:52:04'),
(4, 2, 16, 8, 3, '2024-05-19 12:11:36'),
(5, 2, 19, 8, 3, '2024-05-19 12:11:36'),
(6, 9, 1, 1, 4, '2024-05-22 13:02:57'),
(7, 6, 24, 10, 5, '2024-05-24 20:37:08');

//RESERVATION TABLE 삽입
INSERT INTO DB2024_RESERVATION (reservation_id, customer_id, restaurant_id, reservation_date, reservation_time, reservation_guests, reservation_confirmed) VALUES
(1, 4, 1, '2024-05-15', '17:00:00', 2, 1),
(2, 2, 2, '2024-05-19', '11:00:00', 4, 1),
(3, 2, 8, '2024-05-19', '12:00:00', 6, 1),
(4, 9, 1, '2024-05-22', '13:00:00', 3, 1),
(5, 6, 10, '2024-05-24', '20:00:00', 2, 1),
(6, 8, 5, '2024-05-30', '19:00:00', 2, 0);

//PAYMENT TABLE 삽입
INSERT INTO DB2024_PAYMENT (payment_id, reservation_id, payment_date, payment_type, payment_amount, cash_receipt_requested) VALUES
(1, 1,'2024-05-15 18:22:52', 'card', 20500, NULL),
(2, 2,'2024-05-19 11:37:04', 'cash', 10000, 1),
(3, 4, '2024-05-19 13:25:36', 'card', 8400, NULL),
(4, 3, '2024-05-22 14:19:57', 'cash', 9500, 1),
(5, 5, '2024-05-24 21:28:08', 'card', 46000, NULL);
