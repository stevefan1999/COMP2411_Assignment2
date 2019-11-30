package hk.edu.polyu.comp2411.assignment.view.LoginView

import com.jfoenix.controls.JFXButton
import hk.edu.polyu.comp2411.assignment.entity.StaffEntity
import hk.edu.polyu.comp2411.assignment.entity.StudentEntity
import hk.edu.polyu.comp2411.assignment.service.UserService
import hk.edu.polyu.comp2411.assignment.view.UserView.UserMasterView
import hk.edu.polyu.comp2411.assignment.view.UserView.staff.StaffAllCoursesView
import hk.edu.polyu.comp2411.assignment.view.UserView.staff.StaffAllStudentsView
import hk.edu.polyu.comp2411.assignment.view.UserView.staff.StaffMenuView
import hk.edu.polyu.comp2411.assignment.view.UserView.student.StudentAllCoursesView
import hk.edu.polyu.comp2411.assignment.view.UserView.student.StudentMenuView
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.paint.Paint
import javafx.scene.text.FontWeight
import kfoenix.jfxbutton
import kfoenix.jfxpasswordfield
import kfoenix.jfxtextfield
import tornadofx.*


class LoginView : View("Login") {
    val model = ViewModel()
    val username by lazy { model.bind { SimpleStringProperty() } }
    val password by lazy { model.bind { SimpleStringProperty() } }

    private val userService: UserService by di()

    override fun onUndock() {
        username.value = ""
        password.value = ""
        model.clearDecorators()
    }

    override val root = borderpane {
        center {
            style {
                padding = box(20.px)
            }

            vbox(alignment = Pos.CENTER, spacing = 10) {
                form {
                    fieldset("Welcome to ACME Academy Management System!") {
                        style {
                            fontSize = 20.px
                            alignment = Pos.CENTER
                            fontWeight = FontWeight.BOLD
                        }

                        field("Username") {
                            jfxtextfield(username).required()
                        }
                        field("Password") {
                            jfxpasswordfield {
                                bind(password)
                                required()
                            }
                        }
                    }
                }

                jfxbutton("Login") {
                    style {
                        buttonType = JFXButton.ButtonType.RAISED
                        textFill = Paint.valueOf("white")
                        backgroundColor += Paint.valueOf("blue")
                    }

                    alignment = Pos.CENTER
                    enableWhen(model.valid)
                    action {
                        runAsyncWithProgress {
                            userService.authenticateAndSetUser(
                                username.value,
                                password.value
                            )
                        } ui {
                            val userMasterView = find<UserMasterView>()
                            when (userService.loggedInAs) {
                                is StudentEntity -> {
                                    userMasterView.screenContent.value = find<StudentAllCoursesView>()
                                    userMasterView.sidePane.value = find<StudentMenuView>()
                                    replaceWith(userMasterView)
                                }
                                is StaffEntity -> {
                                    userMasterView.screenContent.value = find<StaffAllCoursesView>()
                                    userMasterView.sidePane.value = find<StaffMenuView>()
                                    replaceWith(userMasterView)
                                }
                                else -> error("Authentication Failure", "User name or password invalid")
                            }
                        }

                    }
                }
            }
        }
    }
}
