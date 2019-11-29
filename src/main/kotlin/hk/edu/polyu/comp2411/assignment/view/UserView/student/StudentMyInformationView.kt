package hk.edu.polyu.comp2411.assignment.view.UserView.student

import com.jfoenix.controls.JFXButton
import hk.edu.polyu.comp2411.assignment.entity.StudentEntity
import hk.edu.polyu.comp2411.assignment.entity.enum.Gender
import hk.edu.polyu.comp2411.assignment.extension.bcrypt
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
    val name by lazy { model.bind { SimpleStringProperty() } }
    val birthday by lazy { model.bind { SimpleObjectProperty<LocalDate>() } }
    val address by lazy { model.bind { SimpleStringProperty() } }
    val gender by lazy { model.bind { SimpleObjectProperty<Gender>() } }

    val passwordViewModel = ViewModel()
    val oldPassword by lazy { passwordViewModel.bind { SimpleStringProperty() } }
    val newPassword by lazy { passwordViewModel.bind { SimpleStringProperty() } }

    override fun onDock() {
        val student = userService.loggedInAs as StudentEntity
        name.value = student.name
        birthday.value = student.birthday
        address.value = student.address
        gender.value = student.gender
        model.clearDecorators()

        oldPassword.value = ""
        newPassword.value = ""
        passwordViewModel.clearDecorators()
    }

    override val root = scrollpane {
        borderpane {
            center {
                form {
                    val student = userService.loggedInAs as StudentEntity
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
                                        label("${student.enrollments?.size}")
                                    }
                                    field("Department") {
                                        label(student.department ?: "N/A")
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

                                            if (!student.password.bcryptCheck(oldPassword.value)) {
                                                error("Update password", "Your provided old password is wrong!")
                                            } else {
                                                val student = userService.loggedInAs as StudentEntity
                                                student.password = newPassword.value.bcrypt()

                                                when (userService.users.save(student)) {
                                                    student -> information("Update password", "Success!")
                                                    else -> error("Update password", "Failure!")
                                                }
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
                            when (userService.users.save(student)) {
                                student -> information("Update data", "Success!")
                                else -> error("Update data", "Failure!")
                            }
                        }
                    }
                }
            }

        }
    }
}
