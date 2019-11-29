package hk.edu.polyu.comp2411.assignment.entity

import javax.persistence.*

@Entity(name = "StaffEntity")
@Table(name = "STAFFS")
@PrimaryKeyJoinColumn(name = "id")
class StaffEntity : UserBaseEntity() {
    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "taughtBy")
    val teachings: Collection<CourseEntity>? = null
}