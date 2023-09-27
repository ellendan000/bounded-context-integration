create table `i_consumed_message`(
    `id` bigint primary key auto_increment,
    `aggregation_id` varchar(36),
    `data` JSON not null,
    `created_time` datetime not null default now()
)