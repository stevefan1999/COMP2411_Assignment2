package hk.edu.polyu.comp2411.assignment.view.UserView.staff

import hk.edu.polyu.comp2411.assignment.service.UserService
import hk.edu.polyu.comp2411.assignment.view.LoginView.LoginView
import hk.edu.polyu.comp2411.assignment.view.UserView.UserMasterView
import kfoenix.jfxbutton
import tornadofx.*

class StaffMenuView : View() {
    private val userService: UserService by di()

    override val root = scrollpane {
        vbox {
            jfxbutton("All courses") {
                action {
                    find<UserMasterView>().screenContent.value = find<StaffAllCoursesView>()
                }
            }
            jfxbutton("All students") {
                action {
                    find<UserMasterView>().screenContent.value = find<StaffAllStudentsView>()
                }
            }

            jfxbutton("Add student") {
                action {
                    find<UserMasterView>().screenContent.value = find<StaffAddNewStudentView>()
                }
            }

            jfxbutton("Add course") {
                action {
                    find<UserMasterView>().screenContent.value = find<StaffAddNewCourseView>()
                }
            }

            jfxbutton("Logout") {
                action {
                    confirm("Logout", "Are you sure you want to log out?") {
                        userService.loggedInAs = null
                        primaryStage.uiComponent<UIComponent>()
                            ?.replaceWith<LoginView>(ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
                    }
                }
            }


            children.style {
                minWidth = 280.px
            }
        }

    }
}
