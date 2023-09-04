REGIONS
INSERT INTO regions(zipcode,city_name,si_gun_gu,dong_eup_myeon)
VALUES ('07217','서울시','영등포구','당산동4가','88');
INSERT INTO regions(zipcode,city_name,si_gun_gu,dong_eup_myeon)
VALUES ('07971','서울시','양천구','목동','524');
INSERT INTO regions(zipcode,city_name,si_gun_gu,dong_eup_myeon)
VALUES ('16702','수원시','영통구','영통동','1024-14');
INSERT INTO regions(zipcode,city_name,si_gun_gu,dong_eup_myeon)
VALUES ('08788','서울시','관악구','봉천동','854-7');
INSERT INTO regions(zipcode,city_name,si_gun_gu,dong_eup_myeon)
VALUES ('07222','서울시','영등포구','당산동6가');
INSERT INTO regions(zipcode,city_name,si_gun_gu,dong_eup_myeon)
VALUES ('07213','서울시','영등포구','당산동5가');
INSERT INTO regions(zipcode,city_name,si_gun_gu,dong_eup_myeon)
VALUES ('07214','서울시','영등포구','당산동5가');
INSERT INTO regions(zipcode,city_name,si_gun_gu,dong_eup_myeon)
VALUES ('05718','서울시','송파구','가락동');
INSERT INTO regions(zipcode,city_name,si_gun_gu,dong_eup_myeon)
VALUES ('05712','서울시','송파구','가락동');
INSERT INTO regions(zipcode,city_name,si_gun_gu,dong_eup_myeon)
VALUES ('06614','서울시','서초구','서초동');
INSERT INTO regions(zipcode,city_name,si_gun_gu,dong_eup_myeon)
VALUES ('04051','서울시','마포구','동교동');
INSERT INTO regions(zipcode,city_name,si_gun_gu,dong_eup_myeon)
VALUES ('04040','서울시','마포구','서교동');

USERS
INSERT INTO users(user_id,id,password,user_name,gender,status,zipcode)
VALUES (SEQ_USER_ID.NEXTVAL,'chy0431','chy12345','최하영',2,1,'07217');
INSERT INTO users(user_id,id,password,user_name,gender,status,zipcode)
VALUES (SEQ_USER_ID.NEXTVAL,'oksh3012','oksh1234','옥승호',1,1,'07971');
INSERT INTO users(user_id,id,password,user_name,gender,status,zipcode)
VALUES (SEQ_USER_ID.NEXTVAL,'danbi52600','danbi123','이단비',2,1,'16702');
INSERT INTO users(user_id,id,password,user_name,gender,status,zipcode)
VALUES (SEQ_USER_ID.NEXTVAL,'easeon78','easeon12','이재성',1,1,'08788');

CATEGORIES
INSERT INTO categories(category_id,category_name)
VALUES (SEQ_CATEGORY_ID.NEXTVAL, '한식');
INSERT INTO categories(category_id,category_name)
VALUES (SEQ_CATEGORY_ID.NEXTVAL, '일식');
INSERT INTO categories(category_id,category_name)
VALUES (SEQ_CATEGORY_ID.NEXTVAL, '중식');
INSERT INTO categories(category_id,category_name)
VALUES (SEQ_CATEGORY_ID.NEXTVAL, '양식');
INSERT INTO categories(category_id,category_name)
VALUES (SEQ_CATEGORY_ID.NEXTVAL, '분식');
INSERT INTO categories(category_id,category_name)
VALUES (SEQ_CATEGORY_ID.NEXTVAL, '아시안');
INSERT INTO categories(category_id,category_name)
VALUES (SEQ_CATEGORY_ID.NEXTVAL, '디저트');
INSERT INTO categories(category_id,category_name)
VALUES (SEQ_CATEGORY_ID.NEXTVAL, '카페');
INSERT INTO categories(category_id,category_name)
VALUES (SEQ_CATEGORY_ID.NEXTVAL, '국밥');
INSERT INTO categories(category_id,category_name)
VALUES (SEQ_CATEGORY_ID.NEXTVAL, '라멘');
INSERT INTO categories(category_id,category_name)
VALUES (SEQ_CATEGORY_ID.NEXTVAL, '마라탕');
INSERT INTO categories(category_id,category_name)
VALUES (SEQ_CATEGORY_ID.NEXTVAL, '쌀국수');
INSERT INTO categories(category_id,category_name)
VALUES (SEQ_CATEGORY_ID.NEXTVAL, '돈까스');
INSERT INTO categories(category_id,category_name)
VALUES (SEQ_CATEGORY_ID.NEXTVAL, '초밥');
INSERT INTO categories(category_id,category_name)
VALUES (SEQ_CATEGORY_ID.NEXTVAL, '술');
INSERT INTO categories(category_id,category_name)
VALUES (SEQ_CATEGORY_ID.NEXTVAL, '호프');
INSERT INTO categories(category_id,category_name)
VALUES (SEQ_CATEGORY_ID.NEXTVAL, '포차');
INSERT INTO categories(category_id,category_name)
VALUES (SEQ_CATEGORY_ID.NEXTVAL, '막걸리');
INSERT INTO categories(category_id,category_name)
VALUES (SEQ_CATEGORY_ID.NEXTVAL, '맥주');
INSERT INTO categories(category_id,category_name)
VALUES (SEQ_CATEGORY_ID.NEXTVAL, '케이크');

FAVORITES
INSERT INTO favorites(restaurant_id,user_id)
VALUES (2,1);
INSERT INTO favorites(restaurant_id,user_id)
VALUES (8,1);
INSERT INTO favorites(restaurant_id,user_id)
VALUES (9,1);
INSERT INTO favorites(restaurant_id,user_id)
VALUES (10,1);
INSERT INTO favorites(restaurant_id,user_id)
VALUES (8,2);
INSERT INTO favorites(restaurant_id,user_id)
VALUES (1,2);
INSERT INTO favorites(restaurant_id,user_id)
VALUES (8,3);
INSERT INTO favorites(restaurant_id,user_id)
VALUES (5,3);
INSERT INTO favorites(restaurant_id,user_id)
VALUES (6,3);

REVIEWS
INSERT INTO reviews(review_id,review_content,rating,write_time,restaurant_id,user_id)
VALUES (SEQ_REVIEW_ID.NEXTVAL,'그냥 평범해요. 육수가 진했으면 좋았을 것 같아요.',3,SYSDATE,4,1);
INSERT INTO reviews(review_id,review_content,rating,write_time,restaurant_id,user_id)
VALUES (SEQ_REVIEW_ID.NEXTVAL,'딸바막걸리 존맛탱!!! 또 먹으러 갈거에요. 전도 너무너무 맛있다',5,SYSDATE,8,1);
INSERT INTO reviews(review_id,review_content,rating,write_time,restaurant_id,user_id)
VALUES (SEQ_REVIEW_ID.NEXTVAL,'맛은 평범한데 컨셉이 특이해요 신비로운 분위기! 사진도 잘찍혀서 종종 갈 듯 합니다',5,SYSDATE,10,1);
INSERT INTO reviews(review_id,review_content,rating,write_time,restaurant_id,user_id)
VALUES (SEQ_REVIEW_ID.NEXTVAL,'맛있는데 항상 오래기다려야 합니다',3,SYSDATE,8,2);
INSERT INTO reviews(review_id,review_content,rating,write_time,restaurant_id,user_id)
VALUES (SEQ_REVIEW_ID.NEXTVAL,'막걸리는 맛있었지만 안주가 별로네요 가격도 비쌉니다',1,SYSDATE,8,4);
INSERT INTO reviews(review_id,review_content,rating,write_time,restaurant_id,user_id)
VALUES (SEQ_REVIEW_ID.NEXTVAL,'운영시간보다 일찍 닫는 경우가 많네요',1,SYSDATE,3,2);
INSERT INTO reviews(review_id,review_content,rating,write_time,restaurant_id,user_id)
VALUES (SEQ_REVIEW_ID.NEXTVAL,'오래된 빵을 파는 것 같아요',1,SYSDATE,3,1);
UPDATE restaurants
SET rating_score = 1
WHERE restaurant_id = 3;
UPDATE restaurants
SET rating_score = 3
WHERE restaurant_id = 4;
UPDATE restaurants
SET rating_score = 3
WHERE restaurant_id = 8;
UPDATE restaurants
SET rating_score = 5
WHERE restaurant_id = 10;




INSERT INTO restaurants(restaurant_id,restaurant_name,detail_address,run_time,zipcode)
VALUES (SEQ_RESTAURANT_ID.NEXTVAL,'버거킹 당산역점','337-23','09:00~23:00','07222');
INSERT INTO restaurants_categories(category_id,restaurant_id)
VALUES (4,1);
INSERT INTO restaurants_categories(category_id,restaurant_id)
VALUES (22,1);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'오리지널스 페퍼잭 싱글 세트',14200,1);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'콰트로치즈와퍼 세트',11300,1);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'몬스터와퍼 세트',12200,1);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'불고기와퍼 세트',10500,1);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'콰트로치즈와퍼',8800,1);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'몬스터와퍼',10200,1);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'불고기와퍼',8000,1);

INSERT INTO restaurants(restaurant_id,restaurant_name,detail_address,run_time,zipcode)
VALUES (SEQ_RESTAURANT_ID.NEXTVAL,'역전할머니맥주 서울당산점','33-5','16:00~04:00','07213');
INSERT INTO restaurants_categories(category_id,restaurant_id)
VALUES (15,2);
INSERT INTO restaurants_categories(category_id,restaurant_id)
VALUES (16,2);
INSERT INTO restaurants_categories(category_id,restaurant_id)
VALUES (19,2);
INSERT INTO restaurants_categories(category_id,restaurant_id)
VALUES (13,2);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'생맥주 300cc',2700,2);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'생맥주 500cc',3300,2);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'생맥주 1000cc',6500,2);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'병맥주',4000,2);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'콜라/사이다',2000,2);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'옛날통닭',12000,2);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'오징어집',7000,2);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'마약치즈돈까스',9000,2);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'치즈라볶이',8000,2);

INSERT INTO restaurants(restaurant_id,restaurant_name,detail_address,run_time,zipcode)
VALUES (SEQ_RESTAURANT_ID.NEXTVAL,'파리바게뜨 당산역삼성점','42','07:00~24:00','07214');
INSERT INTO restaurants_categories(category_id,restaurant_id)
VALUES (7,3);
INSERT INTO restaurants_categories(category_id,restaurant_id)
VALUES (23,3);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'우유퐁당 생크림케이크2호',35000,3);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'납작복숭아 롤케익',15000,3);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'블루베리 쉬폰 케이크',23000,3);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'꽈배기도넛',2300,3); 
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'미니찹쌀도넛',900,3);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'추억의소시지빵',3300,3);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'모카크림식빵',3300,3);


INSERT INTO restaurants(restaurant_id,restaurant_name,detail_address,run_time,zipcode)
VALUES (SEQ_RESTAURANT_ID.NEXTVAL,'필라멘','80 102동 119호','11:00~21:00','05718');
INSERT INTO restaurants_categories(category_id,restaurant_id)
VALUES (2,4);
INSERT INTO restaurants_categories(category_id,restaurant_id)
VALUES (10,4);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'돈코츠라멘',9000,4);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'카라미소라멘',9500,4);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'토리소유라멘',9000,4);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'카레라멘',8000,4);

INSERT INTO restaurants(restaurant_id,restaurant_name,detail_address,run_time,zipcode)
VALUES (SEQ_RESTAURANT_ID.NEXTVAL,'프랭크버거 송파가락점','80 120호','10:30~21:00','05718');
INSERT INTO restaurants_categories(category_id,restaurant_id)
VALUES (4,5);
INSERT INTO restaurants_categories(category_id,restaurant_id)
VALUES (22,5);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'SG불고기버거',3800,5);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'SG불고기버거 세트',7300,5);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'SG크림치즈버거',4800,5);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'SG크림치즈버거 세트',8300,5);

INSERT INTO restaurants(restaurant_id,restaurant_name,detail_address,run_time,zipcode)
VALUES (SEQ_RESTAURANT_ID.NEXTVAL,'지평선초밥 가락본점','84-3','11:00~21:00','05712');
INSERT INTO restaurants_categories(category_id,restaurant_id)
VALUES (2,6);
INSERT INTO restaurants_categories(category_id,restaurant_id)
VALUES (14,6);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'지평선초밥(10ps)',13000,6);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'특초밥(16ps)',20000,6);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'참다랑어 뱃살초밥(10ps)',40000,6);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'연어초밥(11ps)',20000,6);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'광어초밥(11ps)',19000,6);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'광어반연어반초밥(12ps)',21000,6);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'모듬회(35ps)',40000,6);

INSERT INTO restaurants(restaurant_id,restaurant_name,detail_address,run_time,zipcode)
VALUES (SEQ_RESTAURANT_ID.NEXTVAL,'딘딘향 당산점','9-2 1층 102호','11:00~20:00','07213');
INSERT INTO restaurants_categories(category_id,restaurant_id)
VALUES (3,7);
INSERT INTO restaurants_categories(category_id,restaurant_id)
VALUES (11,7);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'마라탕(1인분)',9000,7);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'마라탕(2~3인분)',15000,7);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'마라샹궈(1인분)',14000,7);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'마라샹궈(2~3인분)',19000,7);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'꿔바로우(소)',15000,7);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'꿔바로우(대)',19000,7);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'크림새우',16000,7);

INSERT INTO restaurants(restaurant_id,restaurant_name,detail_address,run_time,zipcode)
VALUES (SEQ_RESTAURANT_ID.NEXTVAL,'금복주류','1307-16 1층','14:00~06:30','06614');
INSERT INTO restaurants_categories(category_id,restaurant_id)
VALUES (15,8);
INSERT INTO restaurants_categories(category_id,restaurant_id)
VALUES (18,8);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'금복알탕',24000,8);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'눈꽃치즈감자전',18000,8);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'골목해물파전',18000,8);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'바질베이컨야채전',18000,8);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'살얼음막걸리(1L)',6000,8);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'자몽살얼음막걸리',5000,8);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'복분자살얼음막걸리',5000,8);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'딸바살얼음막걸리',5000,8);

INSERT INTO restaurants(restaurant_id,restaurant_name,detail_address,run_time,zipcode)
VALUES (SEQ_RESTAURANT_ID.NEXTVAL,'떡볶이상회','169-6 1층','12:00~23:00','04051');
INSERT INTO restaurants_categories(category_id,restaurant_id)
VALUES (5,9);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'로제방울떡도리탕',34000,9);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'방울떡도리탕',32000,9);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'통통떡튀김',6000,9);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'누룽지 순대튀김',7000,9);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'찍먹오뎅튀김',4000,9);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'콜라',2500,9);

INSERT INTO restaurants(restaurant_id,restaurant_name,detail_address,run_time,zipcode)
VALUES (SEQ_RESTAURANT_ID.NEXTVAL,'마녀주방','358-51','12:00~21:30','04040');
INSERT INTO restaurants_categories(category_id,restaurant_id)
VALUES (4,10);
INSERT INTO restaurants_categories(category_id,restaurant_id)
VALUES (21,10);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'리코타유령독초샐러드',7500,10);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'스네이크로제파스타',14500,10);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'레드크림꽃게파스타',13900,10);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'거미줄고구마피자',16900,10);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'비스마르크피자',15900,10);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'마녀의피칵테일',7000,10);
INSERT INTO menu(menu_id,menu_name,menu_price,restaurant_id)
VALUES (SEQ_MENU_ID.NEXTVAL,'트롤의피칵테일',7000,10);


63