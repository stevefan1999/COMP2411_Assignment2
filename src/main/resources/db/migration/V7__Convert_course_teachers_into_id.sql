alter table COURSES
    add (staff_id CHAR(8))
    add constraint "fk: courses::staff_id -> staffs::id" foreign key (staff_id) references STAFFS (id)
;

update COURSES c
set staff_id = (select u.id from USERS u where c.STAFF_NAME = u.NAME)
where exists(select 1 from USERS u where c.STAFF_NAME = u.NAME);

alter table COURSES
    drop (STAFF_NAME);