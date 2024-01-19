alter table if exists employer_favorites add constraint FK9dy3syjvkojvkex5gfvxrrqjk foreign key (favorites_id) references job_seeker_table
;
alter table if exists employer_favorites add constraint FKdo81iey0sh0ovscx5h5b4ef5c foreign key (employers_id) references employer
;