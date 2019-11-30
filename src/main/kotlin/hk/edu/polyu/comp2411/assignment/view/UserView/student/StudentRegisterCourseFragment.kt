package hk.edu.polyu.comp2411.assignment.view.UserView.student

import hk.edu.polyu.comp2411.assignment.controller.CourseController
import hk.edu.polyu.comp2411.assignment.controller.CourseController.AddCourseToStudent
import hk.edu.polyu.comp2411.assignment.controller.CourseController.LoadUnregisteredCourseByStudent
import hk.edu.polyu.comp2411.assignment.controller.EnrollmentController
import hk.edu.polyu.comp2411.assignment.controller.StudentController
import hk.edu.polyu.comp2411.assignment.entity.CourseEntity
import hk.edu.polyu.comp2411.assignment.entity.StudentEntity
import javafx.beans.property.SimpleObjectProperty
import kfoenix.jfxbutton
import tornadofx.*

class StudentRegisterCourseFragment : Fragment("Register course") {
    val student: StudentEntity by param()

    val model = ViewModel()
    val selectedCourse by lazy { model.bind { SimpleObjectProperty<CourseEntity>() } }

    init {
        subscribe<AddCourseToStudent.Event> {
            onDock()
            close()
        }

        find<CourseController>()
        onDock()
    }

    override fun onDock() {
        fire(LoadUnregisteredCourseByStudent.Request(student))
    }

    override val root = borderpane {
        center {
            tableview<CourseEntity> {
                subscribe<LoadUnregisteredCourseByStudent.Event> {
                    items.setAll(it.unregisteredCourses)
                }

                column("#", String::class) {
                    value { it.value.id }
                }
                column("Title", String::class) {
                    value { it.value.title }
                }
                column("Section", String::class) {
                    value { it.value.section }
                }
                column("Teacher", String::class) {
                    value { it.value.taughtBy?.name }
                }

                bindSelected(selectedCourse)

                smartResize()
            }
        }

        bottom {
            borderpane {
                right {
                    jfxbutton("OK") {
                        action {
                            fire(AddCourseToStudent.Request(student, selectedCourse.value))
                        }
                    }
                }
            }
        }
    }
}
