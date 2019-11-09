package hk.edu.polyu.comp2411.assignment.entity

import javax.persistence.*

@Entity(name = "CourseEntity")
@Table(name = "COURSES")
class CourseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COURSE_ID", unique = true, nullable = false)
    lateinit var id: String

    @Column(name = "COURSE_TITLE", nullable = false)
    lateinit var title: String

    @Column(name = "STAFF_NAME", nullable = false)
    lateinit var nameOfStaff: String

    @Column(name = "SECTION", nullable = false)
    lateinit var section: String

    @OneToMany(cascade=[CascadeType.ALL], mappedBy = "courseId", fetch = FetchType.EAGER)
    lateinit var enrollments: List<EnrollmentEntity>

    override fun toString(): String =
        """Entity of type: ${javaClass.name} ( Id = $id Title = $title NameOfStaff = $nameOfStaff Section = $section )"""

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as CourseEntity

        if (id != other.id) return false
        if (title != other.title) return false
        if (nameOfStaff != other.nameOfStaff) return false
        if (section != other.section) return false

        return true
    }

}

