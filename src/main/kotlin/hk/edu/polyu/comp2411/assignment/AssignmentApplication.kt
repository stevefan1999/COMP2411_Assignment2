package hk.edu.polyu.comp2411.assignment

import hk.edu.polyu.comp2411.assignment.repository.CourseRepository
import hk.edu.polyu.comp2411.assignment.repository.EnrollmentRepository
import hk.edu.polyu.comp2411.assignment.repository.StaffRepository
import hk.edu.polyu.comp2411.assignment.repository.StudentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class AssignmentApplication {
	@Autowired lateinit var students: StudentRepository
	@Autowired lateinit var courses: CourseRepository
	@Autowired lateinit var staffs: StaffRepository

	@Bean
	fun init() = CommandLineRunner {
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
	}
}

fun main(args: Array<String>) {
	runApplication<AssignmentApplication>(*args)
}
