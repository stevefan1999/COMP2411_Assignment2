package hk.edu.polyu.comp2411.assignment.view.UserView.staff

import hk.edu.polyu.comp2411.assignment.entity.CourseEntity
import hk.edu.polyu.comp2411.assignment.entity.CourseStudentKeyEntity
import hk.edu.polyu.comp2411.assignment.entity.StudentEntity
import hk.edu.polyu.comp2411.assignment.repository.CourseRepository
import hk.edu.polyu.comp2411.assignment.repository.EnrollmentRepository
import hk.edu.polyu.comp2411.assignment.repository.StudentRepository
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.TableView
import ktfx.collections.toObservableList
import org.springframework.data.repository.findByIdOrNull
import tornadofx.*

class StaffAllCoursesView : View("All courses") {
    val courses: CourseRepository by di()
    val students: StudentRepository by di()
    val enrollmentsRepo: EnrollmentRepository by di()

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
        coursesData.value = null
        coursesData.value = courses.findAll().toObservableList()
        val columns = courseTable?.columns?.get(0)
        columns?.isVisible = false
        columns?.isVisible = true
    }

    fun refreshStudentData() {
        selectedStudent.value = null
        studentData.value = null
        studentData.value = selectedCourse.value?.id?.let { courses.findByIdOrNull(it) }?.enrollments?.map { it.student }?.toObservableList()
        val columns = studentTable?.columns?.get(0)
        columns?.isVisible = false
        columns?.isVisible = true
    }

    init {
        refreshCourseData()

        selectedCourse.addListener {_ ->
            refreshStudentData()
        }
    }

    override fun onDock() {
        refreshCourseData()
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
                        value { it.value.taughtBy?.name }
                    }
                    column("Enrolled students", String::class) {
                        value { it.value.enrollments?.size }
                    }

                    contextmenu {
                        item("Delete course").action {
                            selectedItem?.apply {
                                confirm("Logout", "Are you sure you want delete this student?") {
                                    courses.delete(this)
                                }
                                refreshCourseData()
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
                    column("Department", StudentEntity::department)
                    column("Name", StudentEntity::name)
                    column("Gender", StudentEntity::gender)
                    column("Birthday", StudentEntity::birthday)
                    column("Enrolled Courses", Number::class) {
                        value { it.value.enrollments?.size }
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
                                refreshStudentData()
                                refreshCourseData()
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