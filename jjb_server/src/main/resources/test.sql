insert into User(userId, password) values('a','a');
insert into User(userId, password) values('b','b');
insert into User(userId, password) values('c','c');

insert into Money(userId, usedMoney, totalMoney) values('a',0, 1000);
insert into Money(userId, usedMoney, totalMoney) values('b',0, 1300);
insert into Money(userId, usedMoney, totalMoney) values('c',0, 1000);

insert into Item(id, userId, name, price, isOut, classify, time) values(111,'a','苹果',14.5,1,1,'2015-10-1 12:01:19');
insert into Item(id, userId, name, price, isOut, classify, time) values(112,'a','裙子',55.5,1,0,'2015-10-1 13:01:19');
insert into Item(id, userId, name, price, isOut, classify, time) values(122,'a','工资',1000,0,10,'2015-10-1 13:01:19');
