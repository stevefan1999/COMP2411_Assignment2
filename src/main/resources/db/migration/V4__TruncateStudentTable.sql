alter table students
    drop (STUDENT_NAME, DEPARTMENT, ADDRESS, BIRTHDATE, GENDER);

alter table students
    rename column student_id to id;

alter table students
    add constraint "fk: students::id -> users::id" foreign key (id) references users (id);