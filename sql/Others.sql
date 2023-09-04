-- 프로시저
create or replace PROCEDURE increase_view_count(
    p_restaurant_id IN NUMBER, 
    o_cursor OUT SYS_REFCURSOR
)
IS old_view_count NUMBER;
BEGIN
    SELECT view_count INTO old_view_count FROM restaurants WHERE restaurant_id = p_restaurant_id;

    UPDATE restaurants
    SET view_count = old_view_count+1
    WHERE restaurant_id = p_restaurant_id;
    COMMIT;
    
    OPEN o_cursor FOR
        SELECT *
        FROM (SELECT *
        FROM (SELECT res.restaurant_id, res.restaurant_name, NVL(res.rating_score, -1), res.view_count, res.run_time, res.zipcode, reg.city_name, reg.si_gun_gu, reg.dong_eup_myeon, res.detail_address, LISTAGG(c.category_name, ',') c_name, res.rating_score
        FROM restaurants res
        JOIN regions reg ON res.zipcode = reg.zipcode
        JOIN restaurants_categories rc ON res.restaurant_id = rc.restaurant_id
        JOIN categories c ON c.category_id = rc.category_id
        JOIN menu m ON m.restaurant_id = res.restaurant_id
        GROUP BY res.restaurant_id, res.restaurant_name, res.view_count, res.run_time, res.detail_address, res.zipcode, reg.city_name, reg.si_gun_gu, reg .dong_eup_myeon, res.rating_score)
        WHERE restaurant_id = p_restaurant_id);
         
END increase_view_count;

-- 트리거
create or replace TRIGGER setRating_Score
BEFORE INSERT ON reviews
FOR EACH ROW
DECLARE
    oldSum INT DEFAULT 0;
    oldCount INT DEFAULT 0;
    newSum INT DEFAULT 0;
    newAvg FLOAT DEFAULT 0;
BEGIN
    IF :NEW.rating IS NOT NULL THEN
        SELECT SUM(rating) INTO oldSum FROM reviews WHERE restaurant_id = :NEW.restaurant_id;
        SELECT COUNT(rating) INTO oldCount FROM reviews WHERE restaurant_id = :NEW.restaurant_id;
        IF oldCount = 0 THEN
            newAvg := :NEW.rating;
        ELSE
            newSum := oldSum + :NEW.rating;
            newAvg := newSum / (oldCount + 1);
        END IF;

        UPDATE restaurants
        SET rating_score = newAvg
        WHERE restaurant_id = :NEW.restaurant_id;
    END IF;
END;

-- 시퀀스
CREATE SEQUENCE  "ADMIN"."SEQ_CATEGORY_ID"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 41 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL ;
CREATE SEQUENCE  "ADMIN"."SEQ_MENU_ID"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 101 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL ;
CREATE SEQUENCE  "ADMIN"."SEQ_RESTAURANT_ID"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 41 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL ;
CREATE SEQUENCE  "ADMIN"."SEQ_REVIEW_ID"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 81 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL ;
CREATE SEQUENCE  "ADMIN"."SEQ_USER_ID"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 61 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL ;
