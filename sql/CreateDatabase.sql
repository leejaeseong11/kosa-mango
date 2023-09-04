CREATE TABLE reviews (
	review_id	number,
	review_content	varchar(300),
	rating		number,
	rating_score	number(1,2)	DEFAULT 0,
	write_time	date,
	restaurant_id	number,
	user_id		number,

	CONSTRAINT PK_REVIEWS PRIMARY KEY (review_id),
	CONSTRAINT CK_RATING CHECK(rating IN (1, 3, 5))

);

CREATE TABLE users (
	user_id		number,
	id		varchar2(20)	NOT NULL,
	password	varchar2(20)	NOT NULL,
	user_name	varchar2(30)	NOT NULL,
	gender	number,
	status	number,
	zipcode	char(5),
	
	CONSTRAINT PK_USERS PRIMARY KEY (user_id),
	CONSTRAINT CK_GENDER CHECK(gender IN (1, 2)),
	CONSTRAINT CK_STATUS CHECK(status IN (1, 2))
);

CREATE TABLE categories (
	category_id	number,
	category_name	varchar2(30)	NOT NULL,

	CONSTRAINT PK_CATEGORIES PRIMARY KEY (category_id)
);

CREATE TABLE regions (
	zipcode		char(5),
	city_name	varchar2(30)	NOT NULL,
	si_gun_gu	varchar2(30)	NOT NULL,
	dong_eup_myeon	varchar2(30)	NOT NULL,

	CONSTRAINT PK_REGIONS PRIMARY KEY (zipcode)
);

CREATE TABLE favorites (
	restaurant_id	number,
	user_id		number,

	CONSTRAINT PK_FAVORITES PRIMARY KEY (restaurant_id, user_id)
);

CREATE TABLE restaurants_categories (
	category_id	number,
	restaurant_id	number,

	CONSTRAINT PK_RESTAURANTS_CATEGORIES PRIMARY KEY (category_id, restaurant_id)
);

CREATE TABLE menu (
	menu_id		number,
	menu_name	varchar2(50)	NOT NULL,
	menu_price	number		NOT NULL,
	restaurant_id	number,

	CONSTRAINT PK_MENU PRIMARY KEY (menu_id)
);

CREATE TABLE restaurants (
	restaurant_id	number,
	restaurant_name	varchar(100)	NOT NULL,
	view_count	number,
	rating_score	number(1,2),
	detail_address	varchar(100),
	run_time	char(11),
	zipcode		char(5),

	CONSTRAINT PK_RESTAURANTS PRIMARY KEY (restaurant_id)
);

ALTER TABLE favorites ADD CONSTRAINT FK_restaurants_TO_favorites FOREIGN KEY (
	restaurant_id
)
REFERENCES restaurants (
	restaurant_id
);

ALTER TABLE favorites ADD CONSTRAINT FK_users_TO_favorites FOREIGN KEY (
	user_id
)
REFERENCES users (
	user_id
);

ALTER TABLE restaurants_categories ADD CONSTRAINT FK_categories_TO_r_c FOREIGN KEY (
	category_id
)
REFERENCES categories (
	category_id
);

ALTER TABLE restaurants_categories ADD CONSTRAINT FK_restaurants_TO_r_c FOREIGN KEY (
	restaurant_id
)
REFERENCES restaurants (
	restaurant_id
);

ALTER TABLE restaurants ADD CONSTRAINT FK_regions_TO_restaurants FOREIGN KEY (
	zipcode
)
REFERENCES regions (
	zipcode
);

ALTER TABLE restaurants ADD CONSTRAINT FK_reviews_TO_restaurants FOREIGN KEY (
	rating_score
)
REFERENCES reviews (
	rating_score
);

ALTER TABLE menu ADD CONSTRAINT FK_restaurants_TO_menu FOREIGN KEY (
	restaurant_id
)
REFERENCES restaurants (
	restaurant_id
);

ALTER TABLE reviews ADD CONSTRAINT FK_restaurants_TO_reviews FOREIGN KEY (
	restaurant_id
)
REFERENCES restaurants (
	restaurant_id
);

ALTER TABLE reviews ADD CONSTRAINT FK_users_reviews FOREIGN KEY (
	user_id
)
REFERENCES users (
	user_id
);

ALTER TABLE users ADD CONSTRAINT FK_regions_TO_users FOREIGN KEY (
	zipcode
)
REFERENCES regions (
	zipcode
);

ALTER TABLE regions
MODIFY house_number varchar(10);

ALTER TABLE reviews
DROP COLUMN rating_score;

ALTER TABLE restaurants
MODIFY view_count DEFAULT 0;

ALTER TABLE restaurants
MODIFY rating_score number(2,1);