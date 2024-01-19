alter table if exists vacancies_job_seekers add constraint FKgj502hlg7gi1kb86lpsk61erg foreign key (job_seekers_id) references job_seeker_table
; alter table if exists vacancies_job_seekers add constraint FKgwckmhi79v6ym3sarwux1grn6 foreign key (vacancies_id) references vacancies
;