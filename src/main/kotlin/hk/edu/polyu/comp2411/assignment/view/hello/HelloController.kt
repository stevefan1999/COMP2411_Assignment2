package hk.edu.polyu.comp2411.assignment.view.hello

import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXTextField
import hk.edu.polyu.comp2411.assignment.repository.StudentRepository
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.layout.HBox
import moe.tristan.easyfxml.api.FxmlController
import moe.tristan.easyfxml.util.Buttons.setOnClick
import org.springframework.stereotype.Component

@Component
class HelloController(
    var students: StudentRepository
) : FxmlController {
    @FXML private lateinit var userNameTextField: JFXTextField
    @FXML private lateinit var helloButton: JFXButton
    @FXML private lateinit var greetingBox: HBox
    @FXML private lateinit var greetingName: Label

    override fun initialize() {
        greetingBox.isVisible = false
        userNameTextField.textProperty().addListener { _, _, _ ->
            greet()
        }
        setOnClick(helloButton) { this.greet() }
        println(students.findAll())
    }

    private fun greet() {
        val text = userNameTextField.text
        greetingName.text = if (text.isBlank()) "World" else text
        greetingBox.isVisible = true
    }

}