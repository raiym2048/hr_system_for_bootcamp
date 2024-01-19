alter table if exists profession add constraint FKplyuqu5ynpwnsnnorv15ekuqa foreign key (job_seeker_id) references job_seeker_table
;
alter table if exists profession add constraint FKieo1742a49gj6l1dscpau0tt3 foreign key (position_id) references position
;