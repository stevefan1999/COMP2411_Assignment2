package hk.edu.polyu.comp2411.assignment.view.UserView.staff

import hk.edu.polyu.comp2411.assignment.service.UserService
import tornadofx.View
import tornadofx.borderpane
import tornadofx.center
import tornadofx.label

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
