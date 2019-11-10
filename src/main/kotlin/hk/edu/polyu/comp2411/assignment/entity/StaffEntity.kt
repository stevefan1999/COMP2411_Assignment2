package hk.edu.polyu.comp2411.assignment.entity

import javax.persistence.*

@Entity(name = "StaffEntity")
@Table(name = "STAFFS")
@PrimaryKeyJoinColumn(name = "id")
class StaffEntity : UserBaseEntity() {
    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "taughtBy", fetch = FetchType.EAGER)
    lateinit var teachings: List<CourseEntity>

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as UserBaseEntity
        return this === other
    }
}