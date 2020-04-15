DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Restaurants;


CREATE TABLE IF NOT EXISTS Users
(
	  userName VARCHAR(50) PRIMARY KEY,
	  firstName VARCHAR(50) NOT NULL,
	  lastName VARCHAR(50) NOT NULL,
	  password VARCHAR(50) NOT NULL,
	  email VARCHAR(50) NOT NULL,
	  phone VARCHAR(50) NOT NULL,
	  credit DOUBLE NOT NULL,
);


CREATE TABLE IF NOT EXISTS Restaurants
(
    restaurantId varchar(250) PRIMARY KEY,
    restaurantName varchar(10000) NOT NULL,
    restaurantLogo varchar(10000) NOT NULL,
--     location
)


CREATE TABLE IF NOT EXISTS Foods
(
    foodName varchar(1000) 
)
