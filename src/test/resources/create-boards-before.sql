delete from board where 1 = 1;

insert into board (id, board_name, user_id)
values (1, 'Пробная доска', 1);

alter table board AUTO_INCREMENT=10;