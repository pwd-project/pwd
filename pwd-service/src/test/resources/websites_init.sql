delete from web_site;

copy website from 'd:/websites_init.csv' delimiter ';' CSV quote '"';

select * from  web_site;

-------------------------------------------------------------------------------
insert into website(id, administrative_unit, url) values (1,'Urz�d Gminy �abia Wola','http://www.zabiawola.pl');
insert into website(id, administrative_unit, url) values (2,'Urz�d Miejski w Korszach','http://www.korsze.pl');
insert into website(id, administrative_unit, url) values (3,'Urz�d Miejski w S�popolu','http://bip.warmia.mazury.pl/sepopol_gmina_miejsko_-_wiejska');
insert into website(id, administrative_unit, url) values (4,'Starostwo Powiatowe w Bartoszycach','http://wrota.warmia.mazury.pl/powiat_bartoszycki');
insert into website(id, administrative_unit, url) values (5,'Urz�d Miasta Bartoszyce','http://www.bartoszyce.pl');
insert into website(id, administrative_unit, url) values (6,'Urz�d Marsza�kowski Wojew�dztwa Mazowieckiego','http://www.mazovia.pl');
insert into website(id, administrative_unit, url) values (7,'Urz�d Miasta Sto�ecznego Warszawy','http://www.um.warszawa.pl');