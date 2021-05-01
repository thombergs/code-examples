insert into address (id, city, state, pincode) values
('addr_1', 'chicago', 'TEXAS', 'AB9898');
insert into address (id, city, state, pincode) values
('addr_2', 'hells kitchen', 'CALIFORNIA', 'AB9899');
insert into address (id, city, state, pincode) values
('addr_3', 'palm city', 'TEXAS', 'AA9800');
insert into address (id, city, state, pincode) values
('addr_4', ' San Diego', 'CALIFORNIA', 'AA9230');
insert into address (id, city, state, pincode) values
('addr_5', 'palm city', 'TEXAS', 'AA9560');

insert into distributor (id, name, address_id) values
('dist_1', 'john doe', 'addr_1');
insert into distributor (id, name, address_id) values
('dist_2', 'Max well', 'addr_2');

insert into product
(id, name, price, manufacturing_date, manufacturing_place_id, weight, height, width, distributor_id, category)
values
('prod_1', 'one plus 8T', 100, now(), 'addr_3', 2.2, 3.4, 5.4, 'dist_1', 'MOBILE');

insert into product
(id, name, price, manufacturing_date, manufacturing_place_id, weight, height, width, distributor_id, category)
values
('prod_2', 'Samsung 100 S', 200, now(), 'addr_4', 5.6, 7.9, 4.3, 'dist_2', 'MOBILE');

insert into product
(id, name, price, manufacturing_date, manufacturing_place_id, weight, height, width, distributor_id, category)
values
('prod_3', 'Armani jacket size 32', 1100, now(), 'addr_4', 1.3, 10.4, 29.5, 'dist_2', 'MEN_FASHION');

insert into product
(id, name, price, manufacturing_date, manufacturing_place_id, weight, height, width, distributor_id, category)
values
('prod_4', 'Zara purse', 500, now(), 'addr_5', 3.7, 50.6, 70.6, 'dist_1', 'WOMEN_FASHION');

insert into product
(id, name, price, manufacturing_date, manufacturing_place_id, weight, height, width, distributor_id, category)
values
('prod_5', 'Sony Bravia ', 2000, now(), 'addr_5', 25.5, 48.8, 25.9, 'dist_1', 'TV_APPLIANCES');

insert into product
(id, name, price, manufacturing_date, manufacturing_place_id, weight, height, width, distributor_id, category)
values
('prod_6', 'zara jacket green color', 1500, now(), 'addr_4', 1.3, 10.4, 29.5, 'dist_2', 'MEN_FASHION');