package hk.edu.polyu.comp2411.assignment

import hk.edu.polyu.comp2411.assignment.repository.CourseRepository
import hk.edu.polyu.comp2411.assignment.repository.EnrollmentRepository
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
	@Autowired lateinit var enrollments: EnrollmentRepository

	@Bean
	fun init() = CommandLineRunner {
		students.findAll().forEach { user ->
			println("-------------------------------")
			println("Enrollments for ${user.id}:")
			user.enrollments.forEach { enroll ->
				println(enroll)
			}
			println("-------------------------------")
		}
	}
}

fun main(args: Array<String>) {
	runApplication<AssignmentApplication>(*args)
}
