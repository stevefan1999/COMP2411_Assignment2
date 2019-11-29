package hk.edu.polyu.comp2411.assignment.entity

import javax.persistence.*

@Entity(name = "CourseEntity")
@Table(name = "COURSES")
data class CourseEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    val id: String,

    @Column(name = "TITLE", nullable = false)
    var title: String,

    @Column(name = "SECTION", nullable = false)
    var section: String
) {
    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "course")
    val enrollments: Collection<EnrollmentEntity>? = null

    @ManyToOne
    @JoinColumn(name = "STAFF_ID", referencedColumnName = "id")
    val taughtBy: StaffEntity? = null

    override fun hashCode() = id.hashCode()
}
