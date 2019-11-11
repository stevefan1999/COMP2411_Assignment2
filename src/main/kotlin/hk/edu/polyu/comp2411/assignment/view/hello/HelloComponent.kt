package hk.edu.polyu.comp2411.assignment.view.hello

import moe.tristan.easyfxml.api.FxmlComponent
import moe.tristan.easyfxml.api.FxmlFile
import org.springframework.stereotype.Component

@Component
open class HelloComponent : FxmlComponent {
    override fun getFile() = FxmlFile { "/view/hello/HelloView.fxml" }
    override fun getControllerClass() = HelloController::class.java
}