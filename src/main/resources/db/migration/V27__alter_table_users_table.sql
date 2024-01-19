alter table if exists users_table add constraint FKsm1hxi4uhbi4v6o6w00fr5opf foreign key (employer_id) references employer
; alter table if exists users_table add constraint FKoirnb6ic7xd01mqjhq7gio94i foreign key (job_seeker_id) references job_seeker_table
;alter table if exists users_table add constraint FKbou8y5hdthf5f08490kmy1pwc foreign key (blocked_user_id) references blocked_table
;