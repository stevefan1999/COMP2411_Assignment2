package hk.edu.polyu.comp2411.assignment.view.UserView.staff

import hk.edu.polyu.comp2411.assignment.entity.*
import hk.edu.polyu.comp2411.assignment.repository.CourseRepository
import hk.edu.polyu.comp2411.assignment.repository.EnrollmentRepository
import hk.edu.polyu.comp2411.assignment.repository.StaffRepository
import hk.edu.polyu.comp2411.assignment.repository.StudentRepository
import hk.edu.polyu.comp2411.assignment.service.UserService
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.SelectionMode
import javafx.scene.control.TableView
import kfoenix.jfxbutton
import ktfx.collections.toMutableObservableList
import org.springframework.data.repository.findByIdOrNull
import tornadofx.*
import java.time.LocalDate


class StaffAllCoursesView : View("All courses") {
    class RefreshEvent() : FXEvent()

    class AddStudentToCourseFragment : Fragment("Add students to course...") {
        val courses: CourseRepository by di()
        val students: StudentRepository by di()
        val enrollmentsRepo: EnrollmentRepository by di()

        val course: CourseEntity by param()

        val model = ViewModel()

        val studentData by lazy { model.bind { SimpleListProperty<StudentEntity>() } }
        var studentTable: TableView<StudentEntity>? = null

        fun refreshStudentData() {
            studentData.value = null
            studentData.value =
                (students.findAll().toSet() - (course.id.let { courses.findByIdOrNull(it) }?.enrollments?.map { it.student }?.toSet()
                    ?: emptySet())).toMutableObservableList()
            val columns = studentTable?.columns?.get(0)
            columns?.isVisible = false
            columns?.isVisible = true
        }

        init {
            refreshStudentData()
        }

        fun addStudents() {
            studentTable?.selectionModel?.selectedItems?.forEach {
                val enrollmentKeys = CourseStudentKeyEntity(it.id, course.id)
                val enrollment = EnrollmentEntity(enrollmentKeys, LocalDate.now())
                enrollmentsRepo.save(enrollment)
                fire(RefreshEvent())
                close()
            }

        }

        override val root = borderpane {
            center {
                studentTable = tableview<StudentEntity>(studentData) {
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
            }
            bottom {
                borderpane {
                    right {
                        jfxbutton("Add students") {
                            action {
                                addStudents()
                            }
                        }
                    }
                }
            }
        }

    }

    class ChangeCourseTeacherFragment : Fragment("Change course teacher...") {
        val courses: CourseRepository by di()
        val staffs: StaffRepository by di()

        val course: CourseEntity by param()

        val model = ViewModel()

        val selectedStaff by lazy { model.bind { SimpleObjectProperty<StaffEntity>() } }
        val staffData by lazy { model.bind { SimpleListProperty<StaffEntity>() } }
        var staffTable: TableView<StaffEntity>? = null

        fun refreshStaffData() {
            staffData.value = null
            staffData.value =
                (staffs.findAll().toSet() - course.taughtBy).toMutableObservableList()
            val columns = staffTable?.columns?.get(0)
            columns?.isVisible = false
            columns?.isVisible = true
        }

        init {
            refreshStaffData()
        }

        fun changeTeacher() {
            course.taughtBy = selectedStaff.value
            courses.save(course)
            fire(RefreshEvent())
            close()
        }

        override val root = borderpane {
            center {
                staffTable = tableview<StaffEntity>(staffData) {
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
                                changeTeacher()
                            }
                        }
                    }
                }
            }
        }

    }

    val courses: CourseRepository by di()
    val students: StudentRepository by di()
    val enrollmentsRepo: EnrollmentRepository by di()
    val userService: UserService by di()

    val model = ViewModel()

    val selectedCourse by lazy { model.bind { SimpleObjectProperty<CourseEntity>() } }
    val coursesData by lazy { model.bind { SimpleListProperty<CourseEntity>() } }
    var courseTable: TableView<CourseEntity>? = null

    val selectedStudent by lazy { model.bind { SimpleObjectProperty<StudentEntity>() } }
    val studentData by lazy { model.bind { SimpleListProperty<StudentEntity>() } }
    var studentTable: TableView<StudentEntity>? = null

    fun refreshCourseData() {
        selectedStudent.value = null
        selectedCourse.value = null
        coursesData.value = courses.findAll().toMutableObservableList()
        val columns = courseTable?.columns?.get(0)
        columns?.isVisible = false
        columns?.isVisible = true
    }

    fun refreshStudentData() {
        selectedStudent.value = null
        studentData.value =
            selectedCourse.value?.id?.let { courses.findByIdOrNull(it) }?.enrollments?.map { it.student }
                ?.toMutableObservableList()
        val columns = studentTable?.columns?.get(0)
        columns?.isVisible = false
        columns?.isVisible = true
    }

    init {
        subscribe<RefreshEvent> {
            refreshStudentData()
            refreshCourseData()
        }

        fire(RefreshEvent())

        selectedCourse.addListener { _ ->
            refreshStudentData()
        }
    }

    override fun onDock() {
        fire(RefreshEvent())
    }

    override val root = borderpane {
        center {
            style {
                fontSize = 14.px
            }
            splitpane {
                courseTable = tableview(coursesData) {
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
                                confirm("Logout", "Are you sure you want delete this student?") {
                                    courses.delete(this)
                                }
                                fire(RefreshEvent())
                            }
                        }
                    }

                    bindSelected(selectedCourse)

                    smartResize()
                }

                studentTable = tableview<StudentEntity>(studentData) {
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
                                confirm("Logout", "Are you sure you want remove this student from this course?") {
                                    val items = selectedItem!!.enrollments
                                    if (items != null) {
                                        val enrollment = selectedItem!!.id.let {
                                            selectedCourse.value?.id?.let { it1 ->
                                                CourseStudentKeyEntity(it, it1)
                                            }
                                        }

                                        if (enrollment != null) {
                                            enrollmentsRepo.findByIdOrNull(enrollment)?.let {
                                                enrollmentsRepo.delete(it)
                                            }
                                        }
                                    }
                                }
                                fire(RefreshEvent())
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