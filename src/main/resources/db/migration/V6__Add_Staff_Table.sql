create table staffs
(
    id primary key,
    constraint "fk: staffs::id -> users::id"
        foreign key (id) references USERS (id)
);

insert all
    into users (id, name)
VALUES (id, name)
into staffs (id)
VALUES (id)
select distinct staff_name as name, CONCAT('T', LPAD(TO_CHAR(rownum), 7, '0')) as id
from courses
order by STAFF_NAME;