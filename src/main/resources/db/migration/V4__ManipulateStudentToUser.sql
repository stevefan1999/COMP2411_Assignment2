create table users (
    id	CHAR(8) NOT NULL primary key,
    name	VARCHAR(100) NOT NULL,
    department	VARCHAR(100) NOT NULL,
    address		VARCHAR(255) NOT NULL,
    birthdate	DATE NOT NULL,
    gender		VARCHAR(6) NOT NULL
);

insert into users (id, name, department, address, birthdate, gender)
select STUDENT_ID as id,
    STUDENT_NAME as name,
    DEPARTMENT,
    ADDRESS,
    BIRTHDATE,
    GENDER from STUDENTS;

