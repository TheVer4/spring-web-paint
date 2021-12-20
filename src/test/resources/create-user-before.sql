delete from user_role where 1 = 1;
delete from user where 1 = 1;

insert into user(id, active, password, username)
values (1, true, '123', 'test_user');
insert into user_role (user_id, roles)
values (1, 'USER');

