package hk.edu.polyu.comp2411.assignment.view.UserView.student

import hk.edu.polyu.comp2411.assignment.service.UserService
import hk.edu.polyu.comp2411.assignment.view.LoginView.LoginView
import hk.edu.polyu.comp2411.assignment.view.UserView.UserMasterView
import kfoenix.jfxbutton
import tornadofx.*

class StudentMenuView : View() {
    private val userService: UserService by di()
    override val root = scrollpane {
        vbox {
            jfxbutton("All courses") {
                action {
                    find<UserMasterView>().screenContent.value = find<StudentAllCoursesView>()
                }
            }

            jfxbutton("My courses") {
                action {
                    find<UserMasterView>().screenContent.value = find<StudentMyCoursesView>()
                }
            }

            jfxbutton("Register courses") {
                action {
                    find<UserMasterView>().screenContent.value = find<StudentRegisterCourseView>()
                }
            }

            jfxbutton("My information") {
                action {
                    find<UserMasterView>().screenContent.value = find<StudentMyInformationView>()
                }
            }

            jfxbutton("Logout") {
                action {
                    confirm("Logout", "Are you sure you want to log out?") {
                        userService.loggedInAs = null
                        primaryStage.uiComponent<UIComponent>()?.replaceWith<LoginView>(ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
                    }
                }
            }

            children.style {
                minWidth = 280.px
            }
        }

    }
}
