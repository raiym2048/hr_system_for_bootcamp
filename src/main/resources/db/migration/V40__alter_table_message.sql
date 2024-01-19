alter table if exists message add constraint FKbuqansm1gpu38dksic8gktf7l foreign key (user_messages_id) references user_messages
;alter table if exists message_file_ids add constraint FKcovnr7islvms2xphkmi8hbav2 foreign key (message_id) references message
;