package hk.edu.polyu.comp2411.assignment.service

import hk.edu.polyu.comp2411.assignment.entity.StudentEntity
import hk.edu.polyu.comp2411.assignment.entity.UserBaseEntity
import hk.edu.polyu.comp2411.assignment.extension.bcryptCheck
import hk.edu.polyu.comp2411.assignment.repository.UserBaseRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(
    var users: UserBaseRepository
) {
    var loggedInAs: UserBaseEntity? = null

    fun authenticate(userName: String, password: String): Boolean = try {
        users.findByIdOrNull(userName)?.password?.bcryptCheck(password) ?: false
    } catch (e: Throwable) {
        false
    }

    fun authenticateAndSetUser(userName: String, password: String): Boolean = authenticate(userName, password).apply {
        if (this) loggedInAs = users.findByIdOrNull(userName)
    }


}