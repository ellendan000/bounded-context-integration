create table `redisson_order`(
    id varchar(36) primary key,
    product_id varchar(36) not null,
    quantity int not null default 1,
    created_time datetime default now(),
    last_modified_time datetime default now()
)