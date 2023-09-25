create table `i_inventory_order`(
    id varchar(36) primary key,
    product_id varchar(36) not null,
    quantity int not null default 1,
    created_time datetime not null default now()
)