drop trigger update_merchant_users_timestamp;
alter table contracts drop foreign key FK_CONTRACT_CONTRACTS_MERCHANT;
alter table contracts drop foreign key FK_CONTRACT_CONTRACTS_CAFETERI;
alter table dish_discounts drop foreign key FK_DISH_DIS_DISH_DISC_DISCOUNT;
alter table dish_discounts drop foreign key FK_DISH_DIS_DISH_DISC_DISHES;
alter table dishes drop foreign key FK_DISHES_DISH_CATE_CATEGORI;
alter table dishes drop foreign key FK_DISHES_MERCHANT__MERCHANT;
alter table merchant_users drop foreign key FK_MERCHANT_MERCHANT__USERS;
alter table merchant_users drop foreign key FK_MERCHANT_MERCHANT__MERCHANT;
alter table order_items drop foreign key FK_ORDER_IT_ORDER_ITE_ORDERS;
alter table order_items drop foreign key FK_ORDER_IT_ORDER_ITE_DISHES;
alter table orders drop foreign key FK_ORDERS_REVIEW_OR_REVIEWS;
alter table orders drop foreign key FK_ORDERS_USER_ORDE_USERS;
alter table reviews drop foreign key FK_REVIEWS_USER_REVI_USERS;
alter table user_roles drop foreign key FK_USER_ROL_USER_ROLE_USERS;
alter table user_roles drop foreign key FK_USER_ROL_USER_ROLE_ROLES;
drop table if exists user_menu;
drop table if exists dishes_with_discounts;
drop table if exists cafeterias;
drop table if exists categories;
alter table contracts drop foreign key FK_CONTRACT_CONTRACTS_MERCHANT;
alter table contracts drop foreign key FK_CONTRACT_CONTRACTS_CAFETERI;
drop table if exists contracts;
drop table if exists discounts;
alter table dish_discounts drop foreign key FK_DISH_DIS_DISH_DISC_DISHES;
alter table dish_discounts drop foreign key FK_DISH_DIS_DISH_DISC_DISCOUNT;
drop table if exists dish_discounts;
alter table dishes drop foreign key FK_DISHES_MERCHANT__MERCHANT;
alter table dishes drop foreign key FK_DISHES_DISH_CATE_CATEGORI;
drop table if exists dishes;
alter table merchant_users drop foreign key FK_MERCHANT_MERCHANT__USERS;
alter table merchant_users drop foreign key FK_MERCHANT_MERCHANT__MERCHANT;
drop table if exists merchant_users;
drop table if exists merchants;
alter table order_items drop foreign key FK_ORDER_IT_ORDER_ITE_DISHES;
alter table order_items drop foreign key FK_ORDER_IT_ORDER_ITE_ORDERS;
drop table if exists order_items;
alter table orders drop foreign key FK_ORDERS_USER_ORDE_USERS;
alter table orders drop foreign key FK_ORDERS_REVIEW_OR_REVIEWS;
drop table if exists orders;
alter table reviews drop foreign key FK_REVIEWS_USER_REVI_USERS;
drop table if exists reviews;
drop table if exists roles;
alter table user_roles drop foreign key FK_USER_ROL_USER_ROLE_ROLES;
alter table user_roles drop foreign key FK_USER_ROL_USER_ROLE_USERS;
drop table if exists user_roles;
drop table if exists users;
create table cafeterias (
    cafeteria_id bigint not null comment '',
    name varchar(1024) not null comment '',
    location varchar(1024) not null comment '',
    is_active bool not null comment '',
    primary key (cafeteria_id)
);
create table categories (
    category_id bigint not null comment '',
    name varchar(1024) not null comment '',
    primary key (category_id)
);
create table merchants (
    merchant_id bigint not null comment '',
    name varchar(1024) not null comment '',
    is_active bool not null comment '',
    primary key (merchant_id)
);
create table contracts (
    cafeteria_id bigint not null comment '',
    merchant_id bigint not null comment '',
    start_timestamp timestamp comment '',
    end_timestamp timestamp comment '',
    primary key (cafeteria_id, merchant_id)
);
create table discounts (
    discount_id bigint not null comment '',
    name varchar(1024) not null comment '',
    percentage float not null comment '',
    primary key (discount_id)
);
create table dishes (
    dish_id bigint not null comment '',
    merchant_id bigint comment '',
    category_id bigint comment '',
    name varchar(1024) not null comment '',
    price float not null comment '',
    ingredients text comment '',
    description text comment '',
    primary key (dish_id)
);
create table dish_discounts (
    dish_id bigint not null comment '',
    discount_id bigint not null comment '',
    start_timestamp timestamp not null comment '',
    end_timestamp timestamp not null comment '',
    primary key (dish_id, discount_id)
);
create table users (
    user_id bigint not null comment '',
    username varchar(1024) not null comment '',
    firstname varchar(1024) comment '',
    lastname varchar(1024) comment '',
    id_number varchar(1024) comment '',
    phone varchar(1024) not null comment '',
    password varchar(1024) not null comment '',
    address varchar(1024) comment '',
    email varchar(1024) comment '',
    is_active bool not null comment '',
    salt varchar(1024) not null comment '',
    create_at timestamp comment '',
    primary key (user_id)
);
create table merchant_users (
    merchant_id bigint not null comment '',
    user_id bigint not null comment '',
    update_time timestamp not null comment '',
    job_title varchar(1024) comment '',
    company varchar(1024) comment '',
    primary key (merchant_id, user_id)
);
create table orders (
    order_id bigint not null comment '',
    user_id bigint not null comment '',
    review_id bigint comment '',
    address varchar(1024) not null comment '',
    contact varchar(1024) not null comment '',
    total_price float not null comment '',
    status varchar(1024) not null comment '',
    create_at timestamp not null comment '',
    primary key (order_id)
);
create table order_items (
    dish_id bigint not null comment '',
    order_id bigint not null comment '',
    quantity bigint not null comment '',
    primary key (dish_id, order_id)
);
create table reviews (
    review_id bigint not null comment '',
    user_id bigint not null comment '',
    rating int not null comment '',
    comment text comment '',
    create_at timestamp not null comment '',
    primary key (review_id)
);
create table roles (
    role_id int not null comment '',
    name varchar(1024) not null comment '',
    primary key (role_id)
);
create table user_roles (
    role_id int not null comment '',
    user_id bigint not null comment '',
    grant_date timestamp not null comment '',
    primary key (role_id, user_id)
);
alter table contracts
add constraint FK_CONTRACT_CONTRACTS_MERCHANT foreign key (merchant_id) references merchants (merchant_id) on delete restrict on update restrict;
alter table contracts
add constraint FK_CONTRACT_CONTRACTS_CAFETERI foreign key (cafeteria_id) references cafeterias (cafeteria_id) on delete restrict on update restrict;
alter table dish_discounts
add constraint FK_DISH_DIS_DISH_DISC_DISCOUNT foreign key (discount_id) references discounts (discount_id) on delete restrict on update restrict;
alter table dish_discounts
add constraint FK_DISH_DIS_DISH_DISC_DISHES foreign key (dish_id) references dishes (dish_id) on delete restrict on update restrict;
alter table dishes
add constraint FK_DISHES_DISH_CATE_CATEGORI foreign key (category_id) references categories (category_id) on delete restrict on update restrict;
alter table dishes
add constraint FK_DISHES_MERCHANT__MERCHANT foreign key (merchant_id) references merchants (merchant_id) on delete restrict on update restrict;
alter table merchant_users
add constraint FK_MERCHANT_MERCHANT__USERS foreign key (user_id) references users (user_id) on delete restrict on update restrict;
alter table merchant_users
add constraint FK_MERCHANT_MERCHANT__MERCHANT foreign key (merchant_id) references merchants (merchant_id) on delete restrict on update restrict;
alter table order_items
add constraint FK_ORDER_IT_ORDER_ITE_ORDERS foreign key (order_id) references orders (order_id) on delete restrict on update restrict;
alter table order_items
add constraint FK_ORDER_IT_ORDER_ITE_DISHES foreign key (dish_id) references dishes (dish_id) on delete restrict on update restrict;
alter table orders
add constraint FK_ORDERS_REVIEW_OR_REVIEWS foreign key (review_id) references reviews (review_id) on delete restrict on update restrict;
alter table orders
add constraint FK_ORDERS_USER_ORDE_USERS foreign key (user_id) references users (user_id) on delete restrict on update restrict;
alter table reviews
add constraint FK_REVIEWS_USER_REVI_USERS foreign key (user_id) references users (user_id) on delete restrict on update restrict;
alter table user_roles
add constraint FK_USER_ROL_USER_ROLE_USERS foreign key (user_id) references users (user_id) on delete restrict on update restrict;
alter table user_roles
add constraint FK_USER_ROL_USER_ROLE_ROLES foreign key (role_id) references roles (role_id) on delete restrict on update restrict;