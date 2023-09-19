create table `redisson_product_storage`(
    id varchar(36) primary key,
    product_id varchar(36) not null unique,
    quantity bigint not null default 0,
    created_time datetime not null default now(),
    last_modified_time datetime not null default now()
);

insert into redisson_product_storage values(
    'id-001', 'code-1', 1000, '2023-09-19 19:00:00', '2023-09-19 19:00:00'
);