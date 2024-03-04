insert into kullanici_rol values (
(select id from kullanicilar where kullanici_adi = 'levent'),
(select id from roller where rol = 'ilan_veren'));

insert into kullanici_rol values (
(select id from kullanicilar where kullanici_adi = 'admin'),
(select id from roller where rol = 'admin'));

insert into kullanici_rol values (
(select id from kullanicilar where kullanici_adi = 'admin'),
(select id from roller where rol = 'ilan_veren'));

insert into kullanici_rol values (
(select id from kullanicilar where kullanici_adi = 'admin'),
(select id from roller where rol = 'ilan_arayan'));

insert into kullanici_rol values (
(select id from kullanicilar where kullanici_adi = 'user'),
(select id from roller where rol = 'ilan_arayan'));
