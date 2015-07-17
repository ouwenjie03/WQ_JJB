create database pml;
use pml;

create table User(
	userId INTEGER AUTO_INCREMENT PRIMARY KEY,
	username VARCHAR(10) not null,
	password CHAR(32) not null,
	INDEX username(username)
);

create table Money(
	userId INTEGER NOT NULL PRIMARY KEY,
	totalMoney REAL NOT NULL
);

create table Item(
	itemId VARCHAR(255) PRIMARY KEY,
	userId INTEGER not null,
	name NVARCHAR(255) not null,
	price REAL DEFAULT 0,
	isOut BIT not null,
	classify tinyint not null,
	occurredTime datetime not null,
	modifiedTime datetime not null
);

create table AccessKey(
	userId INTEGER NOT NULL PRIMARY KEY,
	accessKey CHAR(32) NOT NULL,
	expiresTime datetime not null
);

