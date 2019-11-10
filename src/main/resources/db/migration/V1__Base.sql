CREATE TABLE  COURSES (
    COURSE_ID	CHAR(6) NOT NULL primary key,
    COURSE_TITLE	VARCHAR(100) NOT NULL,
    STAFF_NAME	VARCHAR(100) NOT NULL,
    SECTION		CHAR(3) NOT NULL
);

CREATE TABLE STUDENTS (
    STUDENT_ID	CHAR(8) NOT NULL primary key,
    STUDENT_NAME VARCHAR(100) NOT NULL,
    DEPARTMENT	VARCHAR(100) NOT NULL,
    ADDRESS		VARCHAR(255) NOT NULL,
    BIRTHDATE	DATE NOT NULL,
    GENDER		VARCHAR(6) NOT NULL
);

CREATE TABLE ENROLLMENT (
    STUDENT_ID	CHAR(8) NOT NULL,
    COURSE_ID	CHAR(6) NOT NULL,
    REG_DATE	DATE NOT NULL,
    GRADE		NUMBER(3,0) NOT NULL,
    constraint
        "fk: enrollment::student_id -> students::id" foreign key (STUDENT_ID)  references STUDENTS (STUDENT_ID) on delete cascade,
    constraint
        "fk: enrollment::course_id -> courses::id" foreign key (COURSE_ID) references COURSES (COURSE_ID) on delete cascade,
    primary key (STUDENT_ID, COURSE_ID)
);