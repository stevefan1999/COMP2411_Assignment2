package hk.edu.polyu.comp2411.assignment.controller

import hk.edu.polyu.comp2411.assignment.entity.StudentEntity
import hk.edu.polyu.comp2411.assignment.extension.bcrypt
import hk.edu.polyu.comp2411.assignment.repository.CourseRepository
import hk.edu.polyu.comp2411.assignment.repository.StudentRepository
import hk.edu.polyu.comp2411.assignment.service.UserService
import org.springframework.data.repository.findByIdOrNull
import tornadofx.Controller
import tornadofx.EventBus
import tornadofx.EventBus.RunOn.*
import tornadofx.FXEvent

class StudentController : Controller() {
    val courses: CourseRepository by di()
    val students: StudentRepository by di()
    val userService: UserService by di()

    class LoadStudents {
        class Request : FXEvent()

        class Event(
            val students: MutableCollection<StudentEntity>
        ) : FXEvent()
    }

    class LoadStudent {
        class Request(
            val student: StudentEntity
        ) : FXEvent()

        class Event(
            val student: StudentEntity
        ) : FXEvent()
    }

    class ChangePassword {
        class Request(
            val student: StudentEntity,
            val newPlaintextPassword: String
        ) : FXEvent()

        class Event(
            val success: Boolean
        ) : FXEvent()
    }

    class SaveStudent {
        class Request(
            val partialStudent: StudentEntity
        ) : FXEvent()

        class Event(
            val success: Boolean
        ) : FXEvent()
    }

    class DeleteStudent {
        class Request(
            val student: StudentEntity
        ) : FXEvent()

        class Event(
            val success: Boolean
        ) : FXEvent()
    }

    init {
        subscribe<LoadStudent.Request> {
            fire(LoadStudent.Event(it.student))
        }

        subscribe<LoadStudents.Request> {
            fire(LoadStudents.Event(students.findAll().toMutableSet()))
        }

        subscribe<ChangePassword.Request> {
            it.student.password = it.newPlaintextPassword.bcrypt()
            fire(ChangePassword.Event(userService.users.save(it.student) == it.student))
        }

        subscribe<SaveStudent.Request> {
            fire(SaveStudent.Event(students.save(it.partialStudent) == it.partialStudent))
        }

        subscribe<DeleteStudent.Request> {
            val id = it.student.id
            students.delete(it.student)
            fire(DeleteStudent.Event(students.findByIdOrNull(id) == null))
        }

    }
}