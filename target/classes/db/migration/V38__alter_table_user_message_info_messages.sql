alter table if exists user_message_info_user_messages add constraint UK_f0smexd794gl5p4jbg000twda unique (user_messages_id)
;alter table if exists user_message_info_user_messages add constraint FK4k9l496e24rhfm13201wo49lg foreign key (user_messages_id) references user_messages
; alter table if exists user_message_info_user_messages add constraint FKj143x3jcu1fqfxfoh83ivbkot foreign key (user_message_info_id) references user_message_info
;