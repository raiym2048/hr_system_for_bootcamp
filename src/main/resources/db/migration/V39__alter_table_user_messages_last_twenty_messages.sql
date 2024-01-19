alter table if exists user_messages_all_messages add constraint UK_3gpcx3sr3d7mah65waa018du4 unique (all_messages_id)
;
alter table if exists user_messages_all_messages add constraint FKsebkq717t654vns0ujay882qy foreign key (all_messages_id) references message
;
alter table if exists user_messages_all_messages add constraint FKh28yuy7ifxhheh4kpwu1ssfcm foreign key (all_messages_id) references user_messages
;