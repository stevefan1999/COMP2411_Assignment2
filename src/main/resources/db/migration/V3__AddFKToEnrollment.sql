alter table ENROLLMENT
    add foreign key  (STUDENT_ID) references STUDENTS (STUDENT_ID) on delete cascade
    add foreign key (COURSE_ID) references COURSES (COURSE_ID) on delete cascade
    add primary key (STUDENT_ID, COURSE_ID)
;
