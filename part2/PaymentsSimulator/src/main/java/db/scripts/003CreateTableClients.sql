create table Clients (
    id int auto_increment,
    name varchar(50),
	login VARCHAR(50),
	password VARCHAR(50),
	isSuperUser tinyint,
	PRIMARY KEY (id)
);