create table board (
    id integer not null,
    board_name varchar(255),
    user_id bigint,
    primary key (id)
) engine=InnoDB;

create table drawing (
    id bigint not null,
    color varchar(255),
    coords varchar(255),
    drawing_type varchar(255),
    text varchar(255),
    board_id integer,
    primary key (id)
) engine=InnoDB;

create table hibernate_sequence (
    next_val bigint
) engine=InnoDB;

insert into hibernate_sequence values ( 1 );

create table user (
    id bigint not null,
    active bit not null,
    password varchar(255) not null,
    username varchar(255) not null,
    primary key (id)
) engine=InnoDB;

create table user_role (
    user_id bigint not null,
    roles varchar(255)
) engine=InnoDB;

alter table board
    add constraint board_user_fk
        foreign key (user_id)
        references user (id);

alter table drawing
    add constraint drawing_board_fk
        foreign key (board_id)
        references board (id);

alter table user_role
    add constraint user_role_fk
        foreign key (user_id)
        references user (id);