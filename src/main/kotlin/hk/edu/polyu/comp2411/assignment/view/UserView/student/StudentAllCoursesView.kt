package hk.edu.polyu.comp2411.assignment.view.UserView.student

import hk.edu.polyu.comp2411.assignment.entity.CourseEntity
import hk.edu.polyu.comp2411.assignment.repository.CourseRepository
import ktfx.collections.toMutableObservableList
import tornadofx.*

class StudentAllCoursesView : View("All courses") {
    val courses: CourseRepository by di()

    override val root = borderpane {
        center {
            style {
                fontSize = 14.px
            }
            tableview(courses.findAll().toMutableObservableList()) {
                column("#", String::class) {
                    value { it.value.id }
                }
                column("Title", CourseEntity::title)
                column("Section", CourseEntity::section)
                column("Teacher", String::class) {
                    value { it.value.taughtBy?.name }
                }

                smartResize()
            }
        }
    }
}
