package hk.edu.polyu.comp2411.assignment.view.UserView.staff

import com.jfoenix.controls.JFXButton
import hk.edu.polyu.comp2411.assignment.entity.CourseEntity
import hk.edu.polyu.comp2411.assignment.service.CourseService
import hk.edu.polyu.comp2411.assignment.service.StudentService
import hk.edu.polyu.comp2411.assignment.service.UserService
import javafx.beans.property.SimpleStringProperty
import javafx.scene.paint.Paint
import kfoenix.jfxbutton
import kfoenix.jfxtextfield
import tornadofx.*

class StaffAddNewCourseView : View("Add new course...") {
    private val userService: UserService by di()
    private val studentService: StudentService by di()
    private val courseService: CourseService by di()

    val model = ViewModel()
    val identifier by lazy { model.bind { SimpleStringProperty("") } }
    val name by lazy { model.bind { SimpleStringProperty("") } }
    val section by lazy { model.bind { SimpleStringProperty("") } }

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
                                            validator {
                                                if (it?.length!! > 6) {
                                                    error("This field is too long")
                                                } else {
                                                    null
                                                }
                                            }
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
                                            validator {
                                                if (it?.length!! > 3) {
                                                    error("This field is too long")
                                                } else {
                                                    null
                                                }
                                            }
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

                            if (courseService.addCourse(course)) {
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
