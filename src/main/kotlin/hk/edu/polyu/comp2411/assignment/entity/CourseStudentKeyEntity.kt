package hk.edu.polyu.comp2411.assignment.entity

import java.io.Serializable
import javax.persistence.*

@Embeddable
class CourseStudentKeyEntity : Serializable {
    @Id
    @get:Column(name = "STUDENT_ID", nullable = false)
    lateinit var studentId: String

    @Id
    @get:Column(name = "COURSE_ID", nullable = false)
    lateinit var courseId: String

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as CourseStudentKeyEntity

        if (studentId != other.studentId) return false
        if (courseId != other.courseId) return false

        return true
    }

    override fun hashCode() = 42
}