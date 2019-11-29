package hk.edu.polyu.comp2411.assignment.entity

import java.sql.Date
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
    var grade: Byte? = null
) {

    @ManyToOne
    @JoinColumn(name = "STUDENT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    val student: StudentEntity? = null

    @ManyToOne
    @JoinColumn(name = "COURSE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    val course: CourseEntity? = null

    override fun hashCode() = id.hashCode()
}
