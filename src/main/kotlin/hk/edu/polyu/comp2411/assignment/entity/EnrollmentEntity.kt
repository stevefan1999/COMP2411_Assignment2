package hk.edu.polyu.comp2411.assignment.entity

import java.sql.Time
import javax.persistence.*

@Entity(name = "EnrollmentEntity")
@Table(name = "ENROLLMENT")
@IdClass(CourseStudentKeyEntity::class)
class EnrollmentEntity {
    @Id
    @Column(name = "STUDENT_ID", nullable = false)
    private lateinit var studentId: String

    @Id
    @Column(name = "COURSE_ID", nullable = false)
    private lateinit var courseId: String

    @Column(name = "REG_DATE", nullable = false)
    lateinit var registrationDate: Time

    @Column(name = "GRADE", nullable = false)
    var grade: Byte? = null

    @ManyToOne
    @JoinColumn(name = "STUDENT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    lateinit var student: StudentEntity

    @ManyToOne
    @JoinColumn(name = "COURSE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    lateinit var course: CourseEntity

    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( StudentId = ${student.id} CourseId = ${course.id} RegistrationDate = $registrationDate Grade = $grade )"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as EnrollmentEntity

        //if (!id.equals(other)) return false
        if (student != other.student) return false
        if (course != other.course) return false
        if (registrationDate != other.registrationDate) return false
        if (grade != other.grade) return false

        return true
    }

}

