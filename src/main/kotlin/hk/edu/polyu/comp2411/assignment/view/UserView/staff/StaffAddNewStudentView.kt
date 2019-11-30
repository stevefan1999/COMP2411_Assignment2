package hk.edu.polyu.comp2411.assignment.view.UserView.staff

import com.jfoenix.controls.JFXButton
import hk.edu.polyu.comp2411.assignment.entity.StudentEntity
import hk.edu.polyu.comp2411.assignment.entity.enum.Gender
import hk.edu.polyu.comp2411.assignment.extension.bcrypt
import hk.edu.polyu.comp2411.assignment.service.StudentService
import hk.edu.polyu.comp2411.assignment.service.UserService
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

    val model = ViewModel()
    val name by lazy { model.bind { SimpleStringProperty() } }
    val birthday by lazy { model.bind { SimpleObjectProperty<LocalDate>(LocalDate.now()) } }
    val address by lazy { model.bind { SimpleStringProperty() } }
    val department by lazy { model.bind { SimpleStringProperty() } }
    val gender by lazy { model.bind { SimpleObjectProperty<Gender>() } }
    val password by lazy { model.bind { SimpleStringProperty() } }

    override fun onDock() {
        name.value = ""
        birthday.value = LocalDate.now()
        address.value = ""
        department.value = ""
        gender.value = null
        password.value = ""
        model.clearDecorators()
    }

    override val root = scrollpane {
        borderpane {
            center {
                form {
                    hbox(20) {
                        fieldset("Personal Info") {
                            hbox(20) {
                                vbox {
                                    field("Name") {
                                        jfxtextfield {
                                            bind(name)
                                            required()
                                        }
                                    }
                                    field("Birthday") {
                                        jfxdatepicker {
                                            bind(birthday)
                                            required()
                                        }
                                    }
                                    field("Department") {
                                        jfxtextfield {
                                            bind(department)
                                            required()
                                        }
                                    }
                                    field("Gender") {
                                        jfxcombobox<Gender>(
                                            gender,
                                            Gender.values().toObservableList()
                                        ) {
                                            required()
                                        }
                                    }
                                }
                                vbox {
                                    field("Address") {
                                        labelPosition = Orientation.VERTICAL
                                        jfxtextarea {
                                            bind(address)
                                            prefRowCount = 5
                                            vgrow = Priority.ALWAYS
                                            required()
                                        }
                                    }

                                    field("Password") {
                                        jfxpasswordfield {
                                            bind(password)
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
                            val student = StudentEntity()
                            student.name = name.value
                            student.password = password.value.bcrypt()
                            student.birthday = birthday.value
                            student.department = department.value
                            student.address = address.value
                            student.gender = gender.value

                            var persistedStudent = studentService.addStudent(student)
                            if (persistedStudent != null) {
                                information("Add user", "User successfully added as ${persistedStudent.id}!")
                            } else {
                                error("Add user", "Failed to add user!")
                            }

                        }
                    }
                }
            }

        }
    }
}
