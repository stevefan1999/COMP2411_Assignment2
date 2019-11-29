package hk.edu.polyu.comp2411.assignment.view.UserView.staff

import com.jfoenix.controls.JFXButton
import hk.edu.polyu.comp2411.assignment.entity.EnrollmentEntity
import hk.edu.polyu.comp2411.assignment.entity.StudentEntity
import hk.edu.polyu.comp2411.assignment.entity.enum.Gender
import hk.edu.polyu.comp2411.assignment.extension.bcrypt
import hk.edu.polyu.comp2411.assignment.repository.CourseRepository
import hk.edu.polyu.comp2411.assignment.repository.StudentRepository
import hk.edu.polyu.comp2411.assignment.service.UserService
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation
import javafx.scene.control.TableView
import javafx.scene.layout.Priority
import javafx.scene.paint.Paint
import kfoenix.*
import ktfx.collections.toMutableObservableList
import ktfx.collections.toObservableList
import tornadofx.*
import java.sql.Date
import java.time.LocalDate

class StaffAllStudentsView : View("All students") {
    val courses: CourseRepository by di()
    val students: StudentRepository by di()
    val userService: UserService by di()

    val selectionModel = ViewModel()
    val selectedStudent by lazy { selectionModel.bind { SimpleObjectProperty<StudentEntity>() } }
    val selectedStudentEnrollemnts by lazy { selectionModel.bind { SimpleListProperty<EnrollmentEntity>() } }

    val model = ViewModel()
    val name by lazy { model.bind { SimpleStringProperty() } }
    val birthday by lazy { model.bind { SimpleObjectProperty<LocalDate>() } }
    val address by lazy { model.bind { SimpleStringProperty() } }
    val gender by lazy { model.bind { SimpleObjectProperty<Gender>() } }

    val passwordViewModel = ViewModel()
    val newPassword by lazy { passwordViewModel.bind { SimpleStringProperty() } }
    val studentData by lazy { model.bind { SimpleListProperty<StudentEntity>() } }

    var studentTable: TableView<StudentEntity>? = null

    init {
        selectedStudent.addListener {_ ->
            selectedStudentEnrollemnts.value = selectedStudent.value?.enrollments?.toMutableObservableList()
            name.value = selectedStudent.value?.name
            birthday.value = selectedStudent.value?.birthday
            address.value = selectedStudent.value?.address
            gender.value = selectedStudent.value?.gender
            model.clearDecorators()

            newPassword.value = ""
            passwordViewModel.clearDecorators()
        }
    }

    fun refreshStudentData() {
        selectedStudent.value = null
        studentData.value = null
        studentData.value = students.findAll().toMutableObservableList()
        val columns = studentTable?.columns?.get(0)
        columns?.isVisible = false
        columns?.isVisible = true
    }

    override fun onDock() {
        refreshStudentData()
    }

    override val root = borderpane {
        center {
            style {
                fontSize = 14.px
            }
            splitpane {
                studentTable = tableview<StudentEntity>(studentData) {
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
                                confirm("Logout", "Are you sure you want delete this student?") {
                                    students.delete(this)
                                }
                                refreshStudentData()
                            }
                        }
                    }

                    bindSelected(selectedStudent)

                    smartResize()
                }

                splitpane {
                    orientation = Orientation.VERTICAL
                    tableview(selectedStudentEnrollemnts) {
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
                                val taughtBy = it.value.course?.taughtBy
                                when (taughtBy) {
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
                                                            val student = selectedStudent.value
                                                            student.password = newPassword.value.bcrypt()

                                                            when (userService.users.save(student)) {
                                                                student -> information("Update password", "Success!")
                                                                else -> error("Update password", "Failure!")
                                                            }
                                                            refreshStudentData()
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
                                when (userService.users.save(student)) {
                                    student -> information("Update data", "Success!")
                                    else -> error("Update data", "Failure!")
                                }
                                refreshStudentData()
                            }
                        }
                    }
                }
            }
        }
    }
}
