package hk.edu.polyu.comp2411.assignment.entity

import java.sql.Time
import javax.persistence.*

@Entity(name = "EnrollmentEntity")
@Table(name = "ENROLLMENT", schema = "19037626d", catalog = "")
open class EnrollmentEntity {
    @get:EmbeddedId
    lateinit var id: CourseStudentKeyEntity

    @get:Column(name = "REG_DATE", nullable = false)
    lateinit var registrationDate: Time

    @get:Column(name = "GRADE", nullable = false)
    var grade: Byte? = null

    @get:ManyToOne
    lateinit var student: StudentEntity

    @get:ManyToOne
    lateinit var course: CourseEntity

    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( StudentId = ${id.studentId} CourseId = ${id.courseId} RegistrationDate = $registrationDate Grade = $grade )"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as EnrollmentEntity

        if (!id.equals(other)) return false
        if (registrationDate != other.registrationDate) return false
        if (grade != other.grade) return false

        return true
    }

}

