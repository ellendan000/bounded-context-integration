create table `o_event_message`(
    `id` bigint primary key auto_increment,
    `aggregation_id` varchar(36),
    `content` JSON not null,
    `status` varchar(40) not null,
    `created_time` datetime not null default now(),
    `last_modified_time` datetime not null default now()
)