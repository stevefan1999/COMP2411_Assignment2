package hk.edu.polyu.comp2411.assignment.controller

import hk.edu.polyu.comp2411.assignment.entity.*
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
import java.time.LocalDate

class CourseController : Controller() {
    val courses: CourseRepository by di()
    val students: StudentRepository by di()
    val enrollments: EnrollmentRepository by di()
    val userService: UserService by di()

    class LoadCourses {
        class Request : FXEvent()
        class Event(
            val courses: MutableCollection<CourseEntity>
        ) : FXEvent()
    }

    class LoadCourse {
        class Request(
            val course: CourseEntity
        ) : FXEvent()

        class Event(
            val course: CourseEntity
        ) : FXEvent()
    }

    class LoadUnregisteredCourseByStudent {
        class Request(
            val student: StudentEntity
        ) : FXEvent()

        class Event(
            val unregisteredCourses: MutableCollection<CourseEntity>
        ) : FXEvent()
    }

    class LoadUnregisteredStudents {
        class Request(
            val course: CourseEntity
        ) : FXEvent()

        class Event(
            val unregisteredStudents: MutableCollection<StudentEntity>
        ) : FXEvent()
    }

    class AddCourseToStudent {
        class Request(
            val student: StudentEntity,
            val course: CourseEntity
        ) : FXEvent()

        class Event(
            val success: Boolean,
            val student: StudentEntity?
        ) : FXEvent()
    }

    class ChangeCourseTeacher {
        class Request(
            val course: CourseEntity,
            val newTeacher: StaffEntity
        ) : FXEvent()

        class Event(
            val success: Boolean
        ) : FXEvent()
    }

    class DeleteCourse {
        class Request(
            val course: CourseEntity
        ) : FXEvent()

        class Event(
            val success: Boolean
        ) : FXEvent()
    }

    init {
        subscribe<LoadCourses.Request> {
            fire(LoadCourses.Event(courses.findAll().toMutableSet()))
        }

        subscribe<LoadCourse.Request> {
            fire(LoadCourse.Event(it.course))
        }

        subscribe<LoadUnregisteredCourseByStudent.Request> {ev ->
            // find all courses that its enrollments does not have the current selected student

            fire(LoadUnregisteredCourseByStudent.Event(
                (courses.findAll().toSet() - (ev.student.enrollments?.mapNotNull { it.course }?.toSet() ?: emptySet()))
                    .toMutableSet()
            ))
        }

        subscribe<LoadUnregisteredStudents.Request> { ev ->
            // find all students that its enrollments does not have the current selected course

            fire(LoadUnregisteredStudents.Event(
                (students.findAll().toSet() - (ev.course.enrollments?.mapNotNull { it.student }?.toSet() ?: emptySet()))
                    .toMutableSet()
            ))
        }

        subscribe<AddCourseToStudent.Request> {
            val enrollmentKeys = CourseStudentKeyEntity(it.student.id, it.course.id)
            val enrollment = EnrollmentEntity(enrollmentKeys, LocalDate.now())
            val ret = enrollments.save(enrollment)

            when (val search = enrollments.findByIdOrNull(enrollmentKeys)) {
                null -> AddCourseToStudent.Event(false, null)
                else -> AddCourseToStudent.Event(search == ret, search.student)
            }.let { fire(it) }
        }

        subscribe<ChangeCourseTeacher.Request> {
            when (val course = courses.findByIdOrNull(it.course.id)) {
                null -> false
                else -> {
                    course.taughtBy = it.newTeacher
                    courses.save(course) == course
                }
            }.let { ret -> fire(ChangeCourseTeacher.Event(ret)) }

        }

        subscribe<DeleteCourse.Request> {
            val id = it.course.id
            courses.delete(it.course)
            fire(DeleteCourse.Event(courses.findByIdOrNull(id) == null))
        }
    }

}