create table if not exists app_user (
    id serial primary key ,
    username varchar(50) not null default '',
    lastname varchar (100) not null default '',
    firstname varchar (100) not null default '',
    patronymic varchar (100),
    password varchar(200) not null default '',
    email varchar(200),
    enabled bool not null default false
);

create table if not exists role (
    id serial primary key,
    name varchar(20) not null default '',
    description varchar(50) not null default '',
    enabled bool not null default false
);

create table if not exists user_role (
    fk_user_id int not null,
    fk_role_id int not null,
    constraint fk_user_id foreign key (fk_user_id) references app_user(id) on delete cascade on update cascade,
    constraint fk_role_id foreign key (fk_role_id) references role(id) on delete cascade on update cascade
);

create table if not exists book (
    id serial primary key ,
    name varchar(100) not null default '',
    description varchar (300) not null default '',
    genre varchar (50) not null default ''
);

create table if not exists store (
    id serial primary key ,
    name varchar(100) not null default '',
    address varchar (300) not null default ''
);

create table if not exists book_store (
    fk_book_id int not null,
    fk_store_id int not null,
    price double precision not null,
    constraint fk_book_id foreign key (fk_book_id) references book(id) on delete cascade on update cascade,
    constraint fk_store_id foreign key (fk_store_id) references store(id) on delete cascade on update cascade
);

insert into app_user(username, lastname, firstname, patronymic, password, email, enabled)
select 'admin', 'Админов', 'Админ', 'Админович', '$2a$12$HKQHThd.DwmiNuTrpqMYF.SbOk8KHKf96O2KQenGDaZkEu0g0xKqa', 'admin@gmail.com', true
where not exists(
    select 1
    from app_user
    where username = 'admin' and lastname = 'Админов' and firstname = 'Админ' and patronymic = 'Админович'
      and password = '$2a$12$HKQHThd.DwmiNuTrpqMYF.SbOk8KHKf96O2KQenGDaZkEu0g0xKqa' and email = 'admin@gmail.com' and enabled = true);

insert into role (name, description, enabled)
select t_role.name, t_role.description, t_role.enabled
from (
    select 'ROLE_USER' as name, 'Пользователь' as description, true as enabled
    union all
    select 'ROLE_ADMIN', 'Администратор', true) t_role
where not exists(select 1
                 from role r
                 where r.name = t_role.name and r.description = t_role.description and r.enabled = t_role.enabled);

insert into user_role (fk_user_id, fk_role_id)
select t_user_role.fk_user_id, t_user_role.fk_role_id
from (
    select 1 as fk_user_id, 1 as fk_role_id
    union all
    select 1, 2) t_user_role
where not exists(select 1
                 from user_role ur
                 where ur.fk_user_id = t_user_role.fk_user_id and ur.fk_role_id = t_user_role.fk_role_id);