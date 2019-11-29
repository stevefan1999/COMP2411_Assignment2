package hk.edu.polyu.comp2411.assignment.entity

import hk.edu.polyu.comp2411.assignment.entity.enum.Gender
import org.springframework.security.core.userdetails.User
import java.sql.Date
import java.time.LocalDate
import javax.persistence.*

@Entity(name = "UserBaseEntity")
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
abstract class UserBaseEntity {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    lateinit var id: String

    @Column(name = "name", nullable = false)
    lateinit var name: String

    @Column(name = "password", nullable = false)
    lateinit var password: String

    @Column(name = "department", nullable = true)
    var department: String? = null

    @Column(name = "address", nullable = true)
    var address: String? = null

    @Column(name = "birthday", nullable = true)
    var birthday: LocalDate? = null

    @Column(name = "gender", nullable = true)
    @Enumerated(EnumType.STRING)
    var gender: Gender? = null

    override fun toString() =
        "${javaClass.name}(id = $id, name = $name, department = $department, address = $address, birthday = $birthday, gender = $gender)"

    override fun hashCode() = id.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as UserBaseEntity
        if (id != other.id) return false
        if (name != other.name) return false
        if (password != other.password) return false
        if (department != other.department) return false
        if (address != other.address) return false
        if (birthday != other.birthday) return false
        if (gender != other.gender) return false
        return true
    }
}