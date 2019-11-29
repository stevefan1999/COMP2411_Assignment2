package hk.edu.polyu.comp2411.assignment.view.UserView.staff

import hk.edu.polyu.comp2411.assignment.service.UserService
import hk.edu.polyu.comp2411.assignment.view.UserView.UserMasterView
import tornadofx.*

class StaffView : View("Welcome staff") {
    private val userService: UserService by di()

    override val root = borderpane {
        center {
            label {
                text = "staff"
            }
        }
    }
}
