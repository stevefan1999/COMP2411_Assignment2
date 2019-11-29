package hk.edu.polyu.comp2411.assignment.entity

import javax.persistence.*

@Entity(name = "StudentEntity")
@Table(name = "STUDENTS")
@PrimaryKeyJoinColumn(name = "id")
class StudentEntity : UserBaseEntity() {
    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "student")
    var enrollments: MutableCollection<EnrollmentEntity>? = null
}