package hk.edu.polyu.comp2411.assignment.view.UserView.student

import hk.edu.polyu.comp2411.assignment.controller.CourseController
import hk.edu.polyu.comp2411.assignment.controller.CourseController.*
import hk.edu.polyu.comp2411.assignment.entity.CourseEntity
import hk.edu.polyu.comp2411.assignment.entity.StudentEntity
import hk.edu.polyu.comp2411.assignment.service.UserService
import hk.edu.polyu.comp2411.assignment.view.LoginView.LoginView
import hk.edu.polyu.comp2411.assignment.view.UserView.staff.StaffAllCoursesView
import ktfx.layouts.contextMenu
import tornadofx.*

class StudentAllCoursesView : View("All courses") {
    val userService: UserService by di()

    init {
        subscribe<AddCourseToStudent.Event> {
            onDock()
        }

        find<CourseController>()
        onDock()
    }

    override fun onDock() {
        fire(LoadCourses.Request())
    }

    override val root = borderpane {
        center {
            style {
                fontSize = 14.px
            }
            tableview<CourseEntity> {
                subscribe<LoadCourses.Event> {
                    items.setAll(it.courses)
                }

                column("#", String::class) {
                    value { it.value.id }
                }
                column("Title", CourseEntity::title)
                column("Section", CourseEntity::section)
                column("Teacher", String::class) {
                    value { it.value.taughtBy?.name }
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
