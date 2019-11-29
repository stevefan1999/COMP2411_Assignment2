package hk.edu.polyu.comp2411.assignment.view.CounterView

import com.jfoenix.controls.JFXButton
import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Pos
import kfoenix.jfxbutton
import tornadofx.*

class CounterView : View() {
    val counterProperty = SimpleIntegerProperty()
    var counter: Int by counterProperty
    override val root = borderpane {
        center {
            style {
                padding = box(20.px)
            }

            vbox(alignment = Pos.CENTER, spacing = 10) {
                label(counterProperty) {
                    style { fontSize = 20.px }
                }

                jfxbutton("Click to increment", JFXButton.ButtonType.RAISED) {
                    action {
                        counter++
                    }
                }
            }
        }
    }
}