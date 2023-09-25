create table `i_subscribe_order_task`(
    `product_id` varchar(36) primary key,
    `timestamp` bigint not null,
    `current_page` int not null default 1,
    `page_size` int not null default 100
)