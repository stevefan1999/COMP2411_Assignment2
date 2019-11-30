package hk.edu.polyu.comp2411.assignment.view.UserView.student

import com.jfoenix.controls.JFXButton
import hk.edu.polyu.comp2411.assignment.controller.CourseController
import hk.edu.polyu.comp2411.assignment.controller.CourseController.*
import hk.edu.polyu.comp2411.assignment.controller.StudentController
import hk.edu.polyu.comp2411.assignment.controller.StudentController.*
import hk.edu.polyu.comp2411.assignment.entity.StudentEntity
import hk.edu.polyu.comp2411.assignment.entity.enum.Gender
import hk.edu.polyu.comp2411.assignment.extension.bcryptCheck
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

class StudentMyInformationView : View("My information") {
    private val userService: UserService by di()

    val model = ViewModel()
    val name by lazy { model.bind { SimpleStringProperty("") } }
    val birthday by lazy { model.bind { SimpleObjectProperty<LocalDate>() } }
    val address by lazy { model.bind { SimpleStringProperty("") } }
    val gender by lazy { model.bind { SimpleObjectProperty<Gender>() } }

    val passwordViewModel = ViewModel()
    val oldPassword by lazy { passwordViewModel.bind { SimpleStringProperty("") } }
    val newPassword by lazy { passwordViewModel.bind { SimpleStringProperty("") } }

    class ReloadFormData {
        class Request(
            val student: StudentEntity
        ) : FXEvent()

        class Event(
            val student: StudentEntity
        ) : FXEvent()
    }

    init {
        subscribe<ReloadFormData.Event> {
            name.value = it.student.name
            birthday.value = it.student.birthday
            address.value = it.student.address
            gender.value = it.student.gender
            model.clearDecorators()

            oldPassword.value = ""
            newPassword.value = ""
            passwordViewModel.clearDecorators()
        }

        subscribe<ReloadFormData.Request> {
            fire(ReloadFormData.Event(it.student))
        }

        subscribe<SaveStudent.Event> {
            when (it.success) {
                true -> information("Update data", "Success!")
                else -> error("Update data", "Failure!")
            }

            onDock()
        }

        subscribe<ChangePassword.Event> {
            when (it.success) {
                true -> information("Update password", "Success!")
                else -> error("Update password", "Failure!")
            }

            onDock()
        }

        subscribe<AddCourseToStudent.Event> {
            onDock()
        }
    }

    override fun onDock() {
        fire(ReloadFormData.Request(userService.loggedInAs as StudentEntity))
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
                                        jfxtextfield(name)
                                    }
                                    field("Birthday") {
                                        jfxdatepicker(birthday)
                                    }
                                    field("Gender") {
                                        jfxcombobox<Gender>(
                                            gender,
                                            Gender.values().toObservableList()
                                        )
                                    }
                                }
                                vbox {
                                    field("Address") {
                                        labelPosition = Orientation.VERTICAL
                                        jfxtextarea(address) {
                                            prefRowCount = 5
                                            vgrow = Priority.ALWAYS
                                        }
                                    }
                                }
                            }
                        }
                        fieldset("Study information") {
                            hbox(20) {
                                vbox {
                                    field("Registered courses") {
                                        label {
                                            subscribe<ReloadFormData.Event> {
                                                text = "${it.student.enrollments?.size}"
                                            }
                                        }
                                    }
                                    field("Department") {
                                        label {
                                            subscribe<ReloadFormData.Event> {
                                                text = it.student.department ?: "N/A"
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        fieldset("Change password") {
                            hbox(20) {
                                vbox {
                                    field("Old password") {
                                        jfxpasswordfield {
                                            bind(oldPassword)
                                            required()
                                        }
                                    }
                                    field("New password") {
                                        jfxpasswordfield {
                                            bind(newPassword)
                                            required()
                                            validator {
                                                if (it == oldPassword.value) {
                                                    error("The new password cannot be same as the old password")
                                                } else {
                                                    null
                                                }
                                            }
                                        }
                                    }
                                    jfxbutton("Change password") {
                                        enableWhen(passwordViewModel.valid)
                                        style {
                                            buttonType = JFXButton.ButtonType.RAISED
                                            textFill = Paint.valueOf("white")
                                            backgroundColor += Paint.valueOf("blue")
                                        }

                                        action {
                                            val student = userService.loggedInAs as StudentEntity
                                            when (student.password.bcryptCheck(oldPassword.value)) {
                                                true -> fire(
                                                    ChangePassword.Request(
                                                        student,
                                                        newPassword.value
                                                    )
                                                )
                                                else -> error("Update password", "Your provided old password is wrong!")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    jfxbutton("Update") {
                        style {
                            buttonType = JFXButton.ButtonType.RAISED
                            textFill = Paint.valueOf("white")
                            backgroundColor += Paint.valueOf("blue")
                        }

                        action {
                            val student = userService.loggedInAs as StudentEntity
                            student.name = name.value
                            student.birthday = birthday.value
                            student.address = address.value
                            student.gender = gender.value
                            fire(SaveStudent.Request(student))
                        }
                    }
                }
            }

        }
    }
}
