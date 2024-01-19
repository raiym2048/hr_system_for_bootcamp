alter table if exists categories_vacancies add constraint FKvvmk0brsaqyoek0abib74xic foreign key (vacancies_id) references vacancies
;
alter table if exists categories_vacancies add constraint FKe6ctc5s7k20pugqu4uf4evrqu foreign key (category_id) references categories
;