package hk.edu.polyu.comp2411.assignment.view.UserView.staff

import com.jfoenix.controls.JFXButton
import hk.edu.polyu.comp2411.assignment.controller.EnrollmentController
import hk.edu.polyu.comp2411.assignment.controller.EnrollmentController.*
import hk.edu.polyu.comp2411.assignment.controller.StudentController
import hk.edu.polyu.comp2411.assignment.controller.StudentController.*
import hk.edu.polyu.comp2411.assignment.entity.EnrollmentEntity
import hk.edu.polyu.comp2411.assignment.entity.StudentEntity
import hk.edu.polyu.comp2411.assignment.entity.enum.Gender
import hk.edu.polyu.comp2411.assignment.service.UserService
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation
import javafx.scene.layout.Priority
import javafx.scene.paint.Paint
import kfoenix.*
import ktfx.collections.toObservableList
import tornadofx.*
import java.sql.Date
import java.time.LocalDate

class StaffAllStudentsView : View("All students") {
    val userService: UserService by di()

    val model = ViewModel()
    val name by lazy { model.bind { SimpleStringProperty() } }
    val birthday by lazy { model.bind { SimpleObjectProperty<LocalDate>() } }
    val address by lazy { model.bind { SimpleStringProperty() } }
    val gender by lazy { model.bind { SimpleObjectProperty<Gender>() } }

    val passwordViewModel = ViewModel()
    val newPassword by lazy { passwordViewModel.bind { SimpleStringProperty() } }

    val selectedStudent by lazy { model.bind { SimpleObjectProperty<StudentEntity>() } }

    class ChangeGradeFragment: Fragment("Change grade...") {
        val enrollment: EnrollmentEntity by param()

        val model = ViewModel()
        val grade by lazy { model.bind { SimpleIntegerProperty() } }

        init {
            subscribe<SaveEnrollment.Event> {
                close()
            }

            grade.value = enrollment.grade
        }

        override val root = borderpane {
            center {
                label("New Grade")
                jfxtextfield {
                    bind(grade)
                    required()
                    filterInput { it.controlNewText.isInt() }
                    validator {
                        when (if (it != null && it.length >= 0) it.toInt() else null) {
                            !in 0..100 -> error("The new grade must be in the range of [0, 100]")
                            else -> null
                        }
                    }
                }
            }

            bottom {
                right {
                    jfxbutton("Submit") {
                        action {
                            enrollment.grade = grade.value.toByte()
                            fire(SaveEnrollment.Request(enrollment))
                        }
                    }
                }
            }
        }
    }

    class ReloadStudentFormRequest(
        val student: StudentEntity
    ) : FXEvent()

    init {
        subscribe<ReloadStudentFormRequest> {
            name.value = it.student.name
            birthday.value = it.student.birthday
            address.value = it.student.address
            gender.value = it.student.gender
            model.clearDecorators()

            newPassword.value = ""
            passwordViewModel.clearDecorators()
        }

        subscribe<LoadStudents.Event> {
            selectedStudent.value = null
        }

        subscribe<ChangePassword.Event> {
            when (it.success) {
                true -> information("Update password", "Success!")
                else -> error("Update password", "Failure!")
            }

            onDock()
        }

        subscribe<SaveStudent.Event> {
            when (it.success) {
                true -> information("Update data", "Success!")
                else -> error("Update data", "Failure!")
            }

            onDock()
        }

        subscribe<DeleteStudent.Event> {
            when (it.success) {
                true -> information("Delete student", "Success!")
                else -> error("Delete student", "Failure!")
            }

            onDock()
        }

        subscribe<SaveEnrollment.Event> {
            selectedStudent.value = null
            onDock()
        }
    }

    init {
        find<StudentController>()
        onDock()
    }

    override fun onDock() {
        fire(LoadStudents.Request())
    }

    override val root = borderpane {
        center {
            style {
                fontSize = 14.px
            }
            splitpane {
                tableview<StudentEntity> {
                    subscribe<LoadStudents.Event> {
                        items.setAll(it.students)
                    }

                    column("#", String::class) {
                        value { it.value.id }
                    }
                    column("Department", StudentEntity::department)
                    column("Name", StudentEntity::name)
                    column("Gender", StudentEntity::gender)
                    column("Birthday", StudentEntity::birthday)
                    column("Enrolled Courses", Number::class) {
                        value { it.value.enrollments?.size }
                    }
                    column("Average grade", String::class) {
                        value { "%.3f".format(it.value.enrollments?.map { it.grade }?.average()) }
                    }

                    contextmenu {
                        item("Delete student").action {
                            selectedItem?.apply {
                                confirm("Delete", "Are you sure you want delete this student?") {
                                    fire(DeleteStudent.Request(this))
                                }
                            }
                        }
                    }

                    selectionModel.selectedItemProperty().onChange {
                        it?.let {
                            selectedStudent.value = it
                            fire(LoadStudent.Request(it))
                        }
                    }

                    smartResize()
                }

                splitpane {
                    orientation = Orientation.VERTICAL
                    tableview<EnrollmentEntity> {
                        subscribe<LoadStudent.Event> {
                            items.setAll(it.student.enrollments)
                            fire(ReloadStudentFormRequest(it.student))
                        }

                        column("#", String::class) {
                            value { it.value.course?.id }
                        }
                        column("Title", String::class) {
                            value { it.value.course?.title }
                        }
                        column("Section", String::class) {
                            value { it.value.course?.section }
                        }
                        column("Teacher", String::class) {
                            value {
                                when (val taughtBy = it.value.course?.taughtBy) {
                                    userService.loggedInAs -> "Me"
                                    else -> taughtBy?.name
                                }
                            }
                        }
                        column("Grade", Number::class) {
                            value {
                                it.value.grade
                            }
                        }
                        column("Registration date", Date::class) {
                            value { it.value.registrationDate }
                        }

                        contextmenu {
                            item("Change grade").action {
                                selectedItem?.apply {
                                    val modal = find<ChangeGradeFragment>(
                                        mapOf(ChangeGradeFragment::enrollment to selectedItem)
                                    )
                                    openInternalWindow(modal)
                                }
                            }
                        }

                        smartResize()
                    }

                    form {
                        hbox(10) {
                            fieldset("Personal Info") {
                                hbox(10) {
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

                                        fieldset("Change password") {
                                            hbox(10) {
                                                vbox {
                                                    field("New password") {
                                                        jfxpasswordfield {
                                                            bind(newPassword)
                                                            required()
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
                                                            fire(
                                                                ChangePassword.Request(
                                                                    selectedStudent.value,
                                                                    newPassword.value
                                                                )
                                                            )
                                                        }
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
                                val student = selectedStudent.value
                                student.name = name.value
                                student.birthday = birthday.value
                                student.address = address.value
                                student.gender = gender.value
                                fire(SaveStudent.Request(student))
                            }
                        }
                    }
                }
            }
        }
    }
}
