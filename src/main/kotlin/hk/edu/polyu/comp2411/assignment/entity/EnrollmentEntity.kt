package hk.edu.polyu.comp2411.assignment.entity

import java.time.LocalDate
import javax.persistence.*

@Entity(name = "EnrollmentEntity")
@Table(name = "ENROLLMENT")
data class EnrollmentEntity(
    @EmbeddedId
    val id: CourseStudentKeyEntity,

    @Column(name = "REG_DATE", nullable = false)
    var registrationDate: LocalDate,

    @Column(name = "GRADE", nullable = false)
    var grade: Byte = 0
) {

    @ManyToOne
    @JoinColumn(name = "STUDENT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    val student: StudentEntity? = null

    @ManyToOne
    @JoinColumn(name = "COURSE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    val course: CourseEntity? = null

    override fun hashCode() = id.hashCode()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EnrollmentEntity

        if (id != other.id) return false
        if (registrationDate != other.registrationDate) return false
        if (grade != other.grade) return false

        return true
    }
}
