#(RESTAURANT) name & location : 식당의 이름과 위치를 이용해 특정 식당 검색
CREATE INDEX idx_restaurant
ON DB2024_RESTAURANT (restaurant_name, location);

#(CUSTOMER) phone_number : 고객 전화번호로 검색하는 경우가 많은 상황
CREATE INDEX idx_NAME 
ON DB2024_CUSTOMER(phone_number);

#(MENU)category & price: 특정 카테고리의 메뉴를 가격순으로 정렬
CREATE INDEX idx_category_price 
ON DB2024_MENU (menu_category, price);

#(ORDER) restaurant_id & reservation_id : 식당과 예약번호를 이용해 특정 예약 건의 주문내역 검색
CREATE INDEX idx_order
ON DB2024_ORDER (restaurant_id, reservation_id);