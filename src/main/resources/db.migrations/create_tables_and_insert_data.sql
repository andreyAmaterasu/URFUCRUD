create table if not exists users (
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
    constraint fk_user_id foreign key (fk_user_id) references users(id) on delete cascade on update cascade,
    constraint fk_role_id foreign key (fk_role_id) references role(id) on delete cascade on update cascade
);

insert into users(username, lastname, firstname, patronymic, password, email, enabled)
select 'admin', 'Админов', 'Админ', 'Админович', '$2a$12$PWfl/9KGv3SN0XcdFSJ6OueUT9K6uYADhCOv.us3MKtgG0ESrbJsW', 'admin@gmail.com', true
where not exists(
    select 1
    from users
    where username = 'admin' and lastname = 'Админов' and firstname = 'Админ' and patronymic = 'Админович'
      and password = '$2a$12$PWfl/9KGv3SN0XcdFSJ6OueUT9K6uYADhCOv.us3MKtgG0ESrbJsW' and email = 'admin@gmail.com' and enabled = true);

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