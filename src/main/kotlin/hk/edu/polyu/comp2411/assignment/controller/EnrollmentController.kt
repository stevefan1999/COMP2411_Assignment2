package hk.edu.polyu.comp2411.assignment.controller

import hk.edu.polyu.comp2411.assignment.entity.CourseEntity
import hk.edu.polyu.comp2411.assignment.entity.CourseStudentKeyEntity
import hk.edu.polyu.comp2411.assignment.entity.EnrollmentEntity
import hk.edu.polyu.comp2411.assignment.entity.StudentEntity
import hk.edu.polyu.comp2411.assignment.repository.CourseRepository
import hk.edu.polyu.comp2411.assignment.repository.EnrollmentRepository
import hk.edu.polyu.comp2411.assignment.repository.StudentRepository
import hk.edu.polyu.comp2411.assignment.service.UserService
import org.springframework.data.repository.findByIdOrNull
import tornadofx.Controller
import tornadofx.EventBus
import tornadofx.EventBus.RunOn
import tornadofx.EventBus.RunOn.*
import tornadofx.FXEvent

class EnrollmentController : Controller() {
    val courses: CourseRepository by di()
    val students: StudentRepository by di()
    val enrollments: EnrollmentRepository by di()
    val userService: UserService by di()

    class LoadEnrollments {
        class Request : FXEvent()
        class Event(
            val enrollments: MutableCollection<EnrollmentEntity>
        ) : FXEvent()
    }

    class RemoveEnrollment {
        class Request(
            val enrollment: EnrollmentEntity
        ) : FXEvent()

        class Event(
            val success: Boolean
        ) : FXEvent()
    }

    class RemoveEnrollmentByStudentAndCourse {
        class Request(
            val student: StudentEntity,
            val course: CourseEntity
        ) : FXEvent()

        class Event(
            val success: Boolean
        ) : FXEvent()
    }

    class SaveEnrollment {
        class Request(
            val enrollment: EnrollmentEntity
        ) : FXEvent()

        class Event(
            val success: Boolean
        ) : FXEvent()
    }

    init {
        subscribe<LoadEnrollments.Request> {
            fire(LoadEnrollments.Event(enrollments.findAll().toMutableSet()))
        }

        subscribe<RemoveEnrollment.Request> {
            val sid = it.enrollment.student?.id
            val cid = it.enrollment.course?.id

            var enrollmentKey: CourseStudentKeyEntity? = null
            sid?.let {
                cid?.let {
                    enrollmentKey = CourseStudentKeyEntity(sid, cid)
                }
            }

            enrollments.delete(it.enrollment)

            fire(RemoveEnrollment.Event(enrollmentKey?.let { enrollments.findByIdOrNull(it) == null } ?: false))
        }

        subscribe<RemoveEnrollmentByStudentAndCourse.Request> {
            when (val enrollment = enrollments.findByIdOrNull(CourseStudentKeyEntity(it.student.id, it.course.id))) {
                null -> RemoveEnrollment.Event(false)
                else -> RemoveEnrollment.Request(enrollment)
            }.let { ret -> fire(ret) }
        }

        subscribe<SaveEnrollment.Request> {
            fire(SaveEnrollment.Event(enrollments.save(it.enrollment) == it.enrollment))
        }
    }
}