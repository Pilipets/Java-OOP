create table Payments (
    id int auto_increment,
    money int,
	clientId int,
	cardNumber int, 
	info varchar(100),
	PRIMARY KEY (id)
);