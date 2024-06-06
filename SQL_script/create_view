# 영수증 뷰
CREATE VIEW Receipt AS
SELECT 
    P.payment_id,
    P.payment_date,
    P.payment_type,
    P.payment_amount,
    P.cash_receipt_requested,
    RT.restaurant_id,
    RT.restaurant_name,
    RT.location AS restaurant_location,
    RT.contact AS restaurant_contact,
    M.menu_name,
    M.price AS menu_price
FROM DB2024_PAYMENT P
JOIN DB2024_RESERVATION RV ON RV.reservation_id = P.reservation_id
JOIN DB2024_RESTAURANT RT ON RV.restaurant_id = RT.restaurant_id
JOIN DB2024_ORDER O ON O.reservation_id = RV.reservation_id
JOIN DB2024_MENU M ON O.menu_id = M.menu_id;

# 예약 날짜 기준으로 생일이 전후 14일 이내인 고객
CREATE VIEW birthday_coupon AS
SELECT 
    c.customer_name,
    c.phone_number
FROM 
    DB2024_CUSTOMER c
JOIN 
    DB2024_RESERVATION r ON c.customer_id = r.customer_id
WHERE 
    ABS(DATEDIFF(
        DATE_FORMAT(r.reservation_date, '2000-%m-%d'), 
        DATE_FORMAT(c.birthday, '2000-%m-%d')
    )) <= 14;
