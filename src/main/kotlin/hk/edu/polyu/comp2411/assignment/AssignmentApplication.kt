package hk.edu.polyu.comp2411.assignment

import hk.edu.polyu.comp2411.assignment.repository.CourseRepository
import hk.edu.polyu.comp2411.assignment.repository.StaffRepository
import hk.edu.polyu.comp2411.assignment.repository.StudentRepository
import hk.edu.polyu.comp2411.assignment.view.hello.HelloComponent
import javafx.application.Application
import javafx.scene.layout.Pane
import javafx.stage.Stage
import moe.tristan.easyfxml.EasyFxml
import moe.tristan.easyfxml.FxApplication
import moe.tristan.easyfxml.model.beanmanagement.StageManager
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class AssignmentApplication(
    /*
	var students: StudentRepository,
	var courses: CourseRepository,
	var staffs: StaffRepository,
	var easyFxml: EasyFxml,
	var helloComponent: HelloComponent,
	var stageManager: StageManager
     */
) : FxApplication() {
    /*
	lateinit var helloPane: Pane


	override fun start(p0: Stage?) {
		helloPane = easyFxml.load(helloComponent).nodeOrExceptionPane
    }


    @Bean
	override fun init() {
        CommandLineRunner {
            students.findAll().forEach { student ->
                println("")
                println("Student id: ${student.id}")
                println("Student name: ${student.name}")
                println("Student gender: ${student.gender}")
                println("Student address: ${student.address}")
                println("Student birthday: ${student.birthday}")
                println("Student department: ${student.department}")
                println("Student enrollments:")
                student.enrollments.forEach { enroll ->
                    println(enroll)
                }
                println("")
            }

            courses.findAll().forEach { course ->
                println("")
                println("Course id: ${course.id}")
                println("Course title: ${course.title}")
                println("Course section: ${course.section}")
                println("Course taught by: ${course.taughtBy}")
                println("Course enrollers: ${course.enrollments.size}")
                println("")
            }

            staffs.findAll().forEach { staff ->
                println("")
                println("Staff id: ${staff.id}")
                println("Staff name: ${staff.name}")
                println("Staff gender: ${staff.gender}")
                println("Staff address: ${staff.address}")
                println("Staff birthday: ${staff.birthday}")
                println("Staff department: ${staff.department}")
                println("Staff teachings:")
                staff.teachings.forEach { teaching ->
                    println(teaching)
                }
                println("")
            }
        }.run()
    }
    */

	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			launch(AssignmentApplication::class.java)
		}
	}
}
