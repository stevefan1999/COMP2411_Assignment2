package hk.edu.polyu.comp2411.assignment.entity

import hk.edu.polyu.comp2411.assignment.entity.enum.Gender
import java.sql.Time
import javax.persistence.*

@Entity(name = "UserBaseEntity")
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
open class UserBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open lateinit var id: String

    @Column(name = "name", nullable = false)
    open lateinit var name: String

    @Column(name = "department", nullable = true)
    open var department: String? = null

    @Column(name = "address", nullable = true)
    open var address: String? = null

    @Column(name = "birthday", nullable = true)
    open var birthday: Time? = null

    @Column(name = "gender", nullable = true)
    @Enumerated(EnumType.STRING)
    open var gender: Gender? = null

    override fun toString(): String =
        """Entity of type: ${javaClass.name} ( Id = $id Name = $name Department = $department Address = $address Birthday = $birthday Gender = $gender )"""

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as StudentEntity

        if (id != other.id) return false
        if (name != other.name) return false
        if (department != other.department) return false
        if (address != other.address) return false
        if (birthday != other.birthday) return false
        if (gender != other.gender) return false

        return true
    }
}