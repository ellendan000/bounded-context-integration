alter table i_consumed_message add column `event_name` varchar(100) not null after `aggregation_id`;
alter table o_event_message add column `event_name` varchar(100) not null after `aggregation_id`;