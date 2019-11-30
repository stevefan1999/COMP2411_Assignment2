package hk.edu.polyu.comp2411.assignment.controller

import hk.edu.polyu.comp2411.assignment.entity.CourseEntity
import hk.edu.polyu.comp2411.assignment.entity.StaffEntity
import hk.edu.polyu.comp2411.assignment.repository.CourseRepository
import hk.edu.polyu.comp2411.assignment.repository.EnrollmentRepository
import hk.edu.polyu.comp2411.assignment.repository.StaffRepository
import hk.edu.polyu.comp2411.assignment.repository.StudentRepository
import hk.edu.polyu.comp2411.assignment.service.UserService
import tornadofx.Controller
import tornadofx.EventBus
import tornadofx.EventBus.RunOn.*
import tornadofx.FXEvent

class StaffController : Controller() {
    val courses: CourseRepository by di()
    val students: StudentRepository by di()
    val staffs: StaffRepository by di()
    val enrollments: EnrollmentRepository by di()
    val userService: UserService by di()

    class LoadStaffs {
        class Request : FXEvent()
        class Event(
            val staffs: MutableCollection<StaffEntity>
        ) : FXEvent()

    }

    class LoadStaffsNotTeachingCourse {
        class Request(
            val course: CourseEntity
        ) : FXEvent()
        class Event(
            val staffs: MutableCollection<StaffEntity>
        ) : FXEvent()
    }


    init {
        subscribe<LoadStaffs.Request> {
            fire(LoadStaffs.Event(
                staffs.findAll().toMutableSet()
            ))
        }
        subscribe<LoadStaffsNotTeachingCourse.Request> {
            fire(LoadStaffsNotTeachingCourse.Event(
                (staffs.findAll().toSet() - it.course.taughtBy).filterNotNull().toMutableSet()
            ))
        }
    }

}