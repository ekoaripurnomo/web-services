insert into alamat (id, jalan, kota, propinsi, kodepos) values ('1b','Jalan Penggilingan','Cakung','Jakarta Timur',13940);
insert into alamat (id, jalan, kota, propinsi, kodepos) values ('2b','Jalan Komarudin','Cakung','Jakarta Timur',13940);
insert into alamat (id, jalan, kota, propinsi, kodepos) values ('3b','Jalan Duku','Kebon Jeruk','Jakarta Pusat',57890);
insert into alamat (id, jalan, kota, propinsi, kodepos) values ('4b','Jalan Kelapa','Jagakarsa','Jakarta Selatan',23956);
insert into alamat (id, jalan, kota, propinsi, kodepos) values ('5b','Jalan Patriot','Bekasi Barat','Bekasi',47863);

insert into users (username, password, enabled) values ('eko','eko123',true);
insert into users (username, password, enabled) values ('adi','adi123',true);
insert into users (username, password, enabled) values ('edi','edi123',false);

insert into authorities (id_user, authority) values ('1','Admin');
insert into authorities (id_user, authority) values ('1','Operator');
insert into authorities (id_user, authority) values ('2','Operator');
insert into authorities (id_user, authority) values ('3','operator');