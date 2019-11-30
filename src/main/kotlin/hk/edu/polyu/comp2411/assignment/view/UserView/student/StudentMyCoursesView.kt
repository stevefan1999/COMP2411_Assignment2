package hk.edu.polyu.comp2411.assignment.view.UserView.student

import hk.edu.polyu.comp2411.assignment.controller.CourseController
import hk.edu.polyu.comp2411.assignment.controller.CourseController.*
import hk.edu.polyu.comp2411.assignment.controller.EnrollmentController
import hk.edu.polyu.comp2411.assignment.controller.StudentController
import hk.edu.polyu.comp2411.assignment.controller.StudentController.*
import hk.edu.polyu.comp2411.assignment.entity.EnrollmentEntity
import hk.edu.polyu.comp2411.assignment.entity.StudentEntity
import hk.edu.polyu.comp2411.assignment.service.UserService
import ktfx.layouts.contextMenu
import tornadofx.*
import java.sql.Date

class StudentMyCoursesView : View("My courses") {
    val userService: UserService by di()

    init {
        subscribe<AddCourseToStudent.Event> {
            it.student?.let { fire(LoadStudent.Request(it)) }
        }

        find<StudentController>()
        find<EnrollmentController>()
        onDock()
    }

    override fun onDock() {
        fire(LoadStudent.Request(userService.loggedInAs as StudentEntity))
    }

    override val root = borderpane {
        center {
            style {
                fontSize = 14.px
            }
            tableview<EnrollmentEntity> {
                subscribe<LoadStudent.Event> {
                    items.setAll(it.student.enrollments)
                }

                column("#", String::class) {
                    value { it.value.course?.id }
                }
                column("Title", String::class) {
                    value { it.value.course?.title }
                }
                column("Section", String::class) {
                    value { it.value.course?.section }
                }
                column("Teacher", String::class) {
                    value { it.value.course?.taughtBy?.name }
                }
                column("Grade", Number::class) {
                    value { it.value.grade }
                }
                column("Registration date", Date::class) {
                    value { it.value.registrationDate }
                }

                contextMenu {
                    item("Register more course") {
                        action {
                            val modal = find<StudentRegisterCourseFragment>(
                                mapOf(StudentRegisterCourseFragment::student to userService.loggedInAs as StudentEntity)
                            )
                            openInternalWindow(modal)
                        }
                    }
                }

                smartResize()
            }
        }
    }
}
