package hk.edu.polyu.comp2411.assignment.view.UserView.staff

import com.jfoenix.controls.JFXButton
import hk.edu.polyu.comp2411.assignment.entity.CourseEntity
import hk.edu.polyu.comp2411.assignment.entity.StudentEntity
import hk.edu.polyu.comp2411.assignment.entity.enum.Gender
import hk.edu.polyu.comp2411.assignment.extension.bcrypt
import hk.edu.polyu.comp2411.assignment.extension.bcryptCheck
import hk.edu.polyu.comp2411.assignment.repository.CourseRepository
import hk.edu.polyu.comp2411.assignment.service.StudentService
import hk.edu.polyu.comp2411.assignment.service.UserService
import hk.edu.polyu.comp2411.assignment.view.UserView.UserMasterView
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation
import javafx.scene.layout.Priority
import javafx.scene.paint.Paint
import kfoenix.*
import ktfx.collections.toObservableList
import tornadofx.*
import java.time.LocalDate

class StaffAddNewStudentView : View("Welcome staff") {
    private val userService: UserService by di()
    private val studentService: StudentService by di()
    private val courses: CourseRepository by di()

    val model = ViewModel()
    val identifier by lazy { model.bind { SimpleStringProperty() } }
    val name by lazy { model.bind { SimpleStringProperty() } }
    val section by lazy { model.bind { SimpleStringProperty() } }

    override fun onDock() {
        name.value = ""
        identifier.value = ""
        section.value = ""
        model.clearDecorators()
    }

    override val root = scrollpane {
        borderpane {
            center {
                form {
                    hbox(20) {
                        fieldset("Course Info") {
                            hbox(20) {
                                vbox {
                                    field("Course ID") {
                                        jfxtextfield {
                                            bind(identifier)
                                            required()
                                        }
                                    }
                                    field("Course Name") {
                                        jfxtextfield {
                                            bind(name)
                                            required()
                                        }
                                    }
                                    field("Section") {
                                        jfxtextfield {
                                            bind(section)
                                            required()
                                        }
                                    }
                                }
                            }
                        }

                    }
                    jfxbutton("Add Student") {
                        enableWhen(model.valid)

                        style {
                            buttonType = JFXButton.ButtonType.RAISED
                            textFill = Paint.valueOf("white")
                            backgroundColor += Paint.valueOf("blue")
                        }

                        action {
                            val course = CourseEntity(identifier.value, name.value, section.value)

                            var persistedCourse =  courses.save(course)
                            if (persistedCourse != null) {
                                information("Add course", "Course successfully added as ${course.id}!")
                            } else {
                                error("Add course", "Failed to add course!")
                            }

                        }
                    }
                }
            }

        }
    }
}
