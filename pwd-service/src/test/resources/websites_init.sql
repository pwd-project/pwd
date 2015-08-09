delete from web_site;

copy web_site from 'd:/websites_init.csv' delimiter ';' CSV quote '"';

select * from  web_site;