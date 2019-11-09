package hk.edu.polyu.comp2411.assignment.entity

import java.sql.Time
import javax.persistence.*

@Entity(name = "StudentEntity")
@Table(name = "STUDENTS", schema = "19037626d", catalog = "")
open class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @get:Column(name = "STUDENT_ID", nullable = false)
    lateinit var id: String

    @get:Column(name = "STUDENT_NAME", nullable = false)
    lateinit var name: String

    @get:Column(name = "DEPARTMENT", nullable = false)
    lateinit var department: String

    @get:Column(name = "ADDRESS", nullable = false)
    lateinit var address: String

    @get:Column(name = "BIRTHDATE", nullable = false)
    lateinit var birthday: Time

    @get:Column(name = "GENDER", nullable = false)
    lateinit var gender: String

    @get:OneToMany(cascade=[CascadeType.ALL])
    @JoinColumn(name="STUDENT_ID")
    lateinit var enrollments: List<EnrollmentEntity>

    override fun toString(): String =
        """Entity of type: ${javaClass.name} ( Id = $id Name = $name Department = $department Address = $address Birthday = $birthday Gender = $gender )"""

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as StudentEntity

        if (id != other.id) return false
        if (name != other.name) return false
        if (department != other.department) return false
        if (address != other.address) return false
        if (birthday != other.birthday) return false
        if (gender != other.gender) return false

        return true
    }

}

