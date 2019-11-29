package hk.edu.polyu.comp2411.assignment.view.UserView

import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXDrawer
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Insets
import javafx.scene.layout.Priority
import kfoenix.jfxtoolbar
import kfoenix.leftSide
import kfoenix.rightSide
import tornadofx.*

class UserMasterView : View() {
    private var drawerMain: JFXDrawer by singleAssign()
    private var btnDrawer: JFXButton by singleAssign()

    val model = ViewModel()
    val screenContent by lazy { model.bind { SimpleObjectProperty<View>() } }
    val sidePane by lazy { model.bind { SimpleObjectProperty<View>() } }
    val iconButtonDrawer by lazy { model.bind { SimpleObjectProperty(MaterialDesignIconView(MaterialDesignIcon.MENU)) } }
    val contentTitle by lazy { model.bind { SimpleStringProperty() } }

    override fun onUndock() {
        drawerMain.close()
        contentTitle.value = ""
        model.clearDecorators()
    }

    init {
        screenContent.addListener { _ ->
            titleProperty.unbind()
            titleProperty.bind(screenContent.value.titleProperty)
            drawerMain.setContent(screenContent.value.root)
            drawerMain.close()
        }

        sidePane.addListener { _ ->
            drawerMain.setSidePane(sidePane.value.root)
        }

        btnDrawer = JFXButton().apply {
            addClass("button-drawer")
            layoutX = 259.0
            layoutY = 18.0
            graphicProperty().bind(iconButtonDrawer)
            textProperty().bind(contentTitle)

            action {
                drawerMain.apply {
                    when {
                        (isClosed || isClosing) -> open()
                        else -> close()
                    }
                }
            }
        }

        drawerMain = JFXDrawer().apply {
            defaultDrawerSize = 280.0
            direction = JFXDrawer.DrawerDirection.LEFT

            setOnDrawerOpening {
                iconButtonDrawer.value = MaterialDesignIconView(MaterialDesignIcon.ARROW_LEFT)
                timeline {
                    keyframe(200.0.millis) {
                        keyvalue(iconButtonDrawer.value.rotateProperty(), -180)
                    }
                }
            }

            setOnDrawerClosing {
                iconButtonDrawer.value = MaterialDesignIconView(MaterialDesignIcon.MENU)
                timeline {
                    keyframe(200.0.millis) {
                        keyvalue(iconButtonDrawer.value.rotateProperty(), 180)
                    }
                }
            }
        }
    }

    override val root = vbox {
        jfxtoolbar {
            vboxConstraints { margin = Insets(10.0) }
            leftSide {
                this += btnDrawer
            }
            rightSide {

            }
        }
        stackpane {
            vgrow = Priority.ALWAYS
            this += drawerMain
        }
    }

}
