package hk.edu.polyu.comp2411.assignment.entity

import javax.persistence.*

@Entity(name = "StudentEntity")
@Table(name = "STUDENTS")
@PrimaryKeyJoinColumn(name = "id")
class StudentEntity : UserBaseEntity() {
    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "student", fetch = FetchType.EAGER)
    lateinit var enrollments: List<EnrollmentEntity>

    override fun toString(): String =
        """Entity of type: ${javaClass.name} ( Id = $id Name = $name Department = $department Address = $address Birthday = $birthday Gender = $gender )"""

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as UserBaseEntity
        return this === other
    }

}

