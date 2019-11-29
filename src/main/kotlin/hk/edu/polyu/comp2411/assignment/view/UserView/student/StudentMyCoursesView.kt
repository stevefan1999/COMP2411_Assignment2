package hk.edu.polyu.comp2411.assignment.view.UserView.student

import hk.edu.polyu.comp2411.assignment.entity.CourseEntity
import hk.edu.polyu.comp2411.assignment.entity.StudentEntity
import hk.edu.polyu.comp2411.assignment.repository.CourseRepository
import hk.edu.polyu.comp2411.assignment.service.UserService
import ktfx.collections.toMutableObservableList
import tornadofx.*
import java.sql.Date
import java.sql.Time

class StudentMyCoursesView : View("My courses") {
    val userService: UserService by di()

    override val root = borderpane {
        center {
            style {
                fontSize = 14.px
            }
            tableview((userService.loggedInAs as StudentEntity).enrollments?.toMutableObservableList()) {
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

                smartResize()
            }
        }
    }
}
