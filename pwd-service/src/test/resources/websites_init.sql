delete from web_site;

copy website from 'd:/websites_init.csv' delimiter ';' CSV quote '"';

select * from  web_site;

-------------------------------------------------------------------------------
select 'insert into website(id, administrative_unit, url) values ('||id||',"'||administrative_unit||'","'||url||'");' from  website;

insert into website(id, administrative_unit, url, city, voivodeship, county)
values (1,'Urząd Gminy Żabia Wola','http://www.zabiawola.pl','Żabia Wola','Mazowieckie','Grodziski');
insert into website(id, administrative_unit, url)values (2,'Urząd Miejski w Korszach','http://www.korsze.pl');
insert into website(id, administrative_unit, url) values (3,'Urząd Miejski w Sępopolu','http://bip.warmia.mazury.pl/sepopol_gmina_miejsko_-_wiejska');
insert into website(id, administrative_unit, url) values (4,'Starostwo Powiatowe w Bartoszycach','http://wrota.warmia.mazury.pl/powiat_bartoszycki');
insert into website(id, administrative_unit, url) values (5,'Urząd Miasta Bartoszyce','http://www.bartoszyce.pl');
insert into website(id, administrative_unit, url) values (6,'Urząd Marszałkowski Województwa Mazowieckiego','http://www.mazovia.pl');
insert into website(id, administrative_unit, url, city, voivodeship, county)
values (7,'Urząd Miasta Stołecznego Warszawy','http://www.um.warszawa.pl','Warszawa','Mazowieckie','Warszawski');