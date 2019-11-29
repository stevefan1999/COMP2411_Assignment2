package hk.edu.polyu.comp2411.assignment.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class CourseStudentKeyEntity(
    @Column(name = "STUDENT_ID", nullable = false)
    val studentId: String,

    @Column(name = "COURSE_ID", nullable = false)
    val courseId: String
) : Serializable