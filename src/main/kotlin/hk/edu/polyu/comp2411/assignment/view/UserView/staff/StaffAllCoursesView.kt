package hk.edu.polyu.comp2411.assignment.view.UserView.staff

import hk.edu.polyu.comp2411.assignment.controller.CourseController
import hk.edu.polyu.comp2411.assignment.controller.CourseController.*
import hk.edu.polyu.comp2411.assignment.controller.EnrollmentController
import hk.edu.polyu.comp2411.assignment.controller.EnrollmentController.RemoveEnrollment
import hk.edu.polyu.comp2411.assignment.controller.EnrollmentController.RemoveEnrollmentByStudentAndCourse
import hk.edu.polyu.comp2411.assignment.controller.StaffController
import hk.edu.polyu.comp2411.assignment.controller.StaffController.*
import hk.edu.polyu.comp2411.assignment.entity.CourseEntity
import hk.edu.polyu.comp2411.assignment.entity.StaffEntity
import hk.edu.polyu.comp2411.assignment.entity.StudentEntity
import hk.edu.polyu.comp2411.assignment.service.UserService
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.SelectionMode
import javafx.scene.control.TableView
import kfoenix.jfxbutton
import tornadofx.*


class StaffAllCoursesView : View("All courses") {
    class AddStudentToCourseFragment : Fragment("Add students to course...") {
        val course: CourseEntity by param()
        val model = ViewModel()
        val selectedStudents by lazy { model.bind { SimpleListProperty<StudentEntity>() } }

        var studentTable: TableView<StudentEntity> by singleAssign()

        init {
            studentTable = tableview<StudentEntity> {
                subscribe<LoadUnregisteredStudents.Event> {
                    items.setAll(it.unregisteredStudents)
                }

                selectionModel.selectionMode = SelectionMode.MULTIPLE
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
                smartResize()
            }

            find<StaffController>()

            subscribe<AddCourseToStudent.Event> {
                close()
            }

            onDock()
        }

        override fun onDock() {
            fire(LoadUnregisteredStudents.Request(course))
        }

        override val root = borderpane {
            center {
                this += studentTable
            }
            bottom {
                borderpane {
                    right {
                        jfxbutton("Add students") {
                            action {
                                studentTable.selectionModel.selectedItems.forEach {
                                    fire(AddCourseToStudent.Request(it, course))
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    class ChangeCourseTeacherFragment : Fragment("Change course teacher...") {
        val course: CourseEntity by param()
        val model = ViewModel()
        val selectedStaff by lazy { model.bind { SimpleObjectProperty<StaffEntity>() } }

        init {
            find<StaffController>()
            subscribe<ChangeCourseTeacher.Event> {
                close()
            }
            onDock()
        }

        override fun onDock() {
            fire(LoadStaffsNotTeachingCourse.Request(course))
        }

        override val root = borderpane {
            center {
                tableview<StaffEntity> {
                    subscribe<LoadStaffsNotTeachingCourse.Event> {
                        items.setAll(it.staffs)
                    }

                    column("#", String::class) {
                        value { it.value.id }
                    }
                    column("Department", StaffEntity::department)
                    column("Name", StaffEntity::name)
                    column("Gender", StaffEntity::gender)
                    column("Birthday", StaffEntity::birthday)

                    bindSelected(selectedStaff)

                    smartResize()
                }
            }
            bottom {
                borderpane {
                    right {
                        jfxbutton("Change teacher") {
                            action {
                                fire(ChangeCourseTeacher.Request(course, selectedStaff.value))
                            }
                        }
                    }
                }
            }
        }

    }

    val model = ViewModel()

    val selectedCourse by lazy { model.bind { SimpleObjectProperty<CourseEntity>() } }
    val selectedStudent by lazy { model.bind { SimpleObjectProperty<StudentEntity>() } }

    val userService: UserService by di()

    init {
        find<CourseController>()
        find<EnrollmentController>()

        subscribe<ChangeCourseTeacher.Event> {
            selectedStudent.value = null
            selectedCourse.value = null
            onDock()
        }

        subscribe<DeleteCourse.Event> {
            selectedStudent.value = null
            selectedCourse.value = null
            onDock()
        }

        subscribe<RemoveEnrollment.Event> {
            selectedStudent.value = null
            selectedCourse.value = null
            onDock()
        }

        subscribe<AddCourseToStudent.Event> {
            onDock()
        }

        onDock()
    }

    override fun onDock() {
        fire(LoadCourses.Request())
    }

    override val root = borderpane {
        center {
            style {
                fontSize = 14.px
            }
            splitpane {
                tableview<CourseEntity> {
                    subscribe<LoadCourses.Event> {
                        items.setAll(it.courses)
                    }

                    column("#", String::class) {
                        value { it.value.id }
                    }
                    column("Title", CourseEntity::title)
                    column("Section", CourseEntity::section)
                    column("Teacher", String::class) {
                        value {
                            when (val taughtBy = it.value.taughtBy) {
                                userService.loggedInAs -> "Me"
                                else -> taughtBy?.name
                            }
                        }
                    }
                    column("Enrolled students", String::class) {
                        value { it.value.enrollments?.size }
                    }
                    column("Average grade", String::class) {
                        value { "%.3f".format(it.value.enrollments?.map { it.grade }?.average()) }
                    }

                    contextmenu {
                        item("Add student to this course").action {
                            selectedItem?.apply {
                                val modal = find<AddStudentToCourseFragment>(
                                    mapOf(AddStudentToCourseFragment::course to selectedItem)
                                )
                                openInternalWindow(modal)
                            }
                        }
                        item("Change teacher").action {
                            selectedItem?.apply {
                                val modal = find<ChangeCourseTeacherFragment>(
                                    mapOf(ChangeCourseTeacherFragment::course to selectedItem)
                                )
                                openInternalWindow(modal)
                            }
                        }
                        item("Delete course").action {
                            selectedItem?.apply {
                                confirm("Delete course", "Are you sure to delete this course?") {
                                    fire(DeleteCourse.Request(this))
                                }
                            }
                        }
                    }

                    selectionModel.selectedItemProperty().onChange {
                        selectedCourse.value = it
                        it?.let { fire(LoadCourse.Request(it)) }
                    }

                    smartResize()
                }

                tableview<StudentEntity> {
                    subscribe<LoadCourse.Event> {
                        items.setAll(it.course.enrollments?.map { it.student })
                    }

                    column("#", String::class) {
                        value { it.value.id }
                    }
                    column("Name", StudentEntity::name)
                    column("Gender", StudentEntity::gender)
                    column("Grade", Number::class) {
                        value { it.value.enrollments?.firstOrNull { it2 -> it2.course == selectedCourse.value }?.grade }
                    }

                    contextmenu {
                        item("Remove student from course").action {
                            selectedItem?.apply {
                                confirm("Kick student", "Are you sure you want remove this student from this course?") {
                                    fire(
                                        RemoveEnrollmentByStudentAndCourse.Request(
                                            selectedItem!!,
                                            selectedCourse.value
                                        )
                                    )
                                }
                            }
                        }
                    }

                    bindSelected(selectedStudent)

                    smartResize()
                }
            }
        }
    }
}