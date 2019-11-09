package hk.edu.polyu.comp2411.assignment.entity

import java.sql.Time
import javax.persistence.*

@Entity(name = "StudentEntity")
@Table(name = "STUDENTS")
class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STUDENT_ID", nullable = false)
    lateinit var id: String

    @Column(name = "STUDENT_NAME", nullable = false)
    lateinit var name: String

    @Column(name = "DEPARTMENT", nullable = false)
    lateinit var department: String

    @Column(name = "ADDRESS", nullable = false)
    lateinit var address: String

    @Column(name = "BIRTHDATE", nullable = false)
    lateinit var birthday: Time

    @Column(name = "GENDER", nullable = false)
    lateinit var gender: String

    @OneToMany(cascade=[CascadeType.ALL], mappedBy = "studentId", fetch = FetchType.EAGER)
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

