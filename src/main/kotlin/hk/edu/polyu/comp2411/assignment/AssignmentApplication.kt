package hk.edu.polyu.comp2411.assignment

import hk.edu.polyu.comp2411.assignment.view.LoginView.LoginView
import javafx.application.Application
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import tornadofx.*
import kotlin.reflect.KClass

class MyStylesheet : Stylesheet() {
    init {
        root {
            fontFamily = "Roboto Light"
            fontSize = 20.px
        }
    }
}

@SpringBootApplication
class AssignmentApplication : App(LoginView::class, MyStylesheet::class) {
    init {
        reloadStylesheetsOnFocus()
    }

    override fun init() {
        val ctx = SpringApplication.run(this::class.java)
        FX.dicontainer = object : DIContainer {
            override fun <T : Any> getInstance(type: KClass<T>): T = ctx.getBean(type.java)
            override fun <T : Any> getInstance(type: KClass<T>, name: String): T = ctx.getBean(type.java, name)
        }


        /*
        val students = ctx.getBean<StudentRepository>()
        val courses = ctx.getBean<CourseRepository>()
        val staffs = ctx.getBean<StaffRepository>()
        val userService = ctx.getBean<UserService>()

        CommandLineRunner {
            students.findAll().forEach {
                println("")
                println("Student id: ${it.id}")
                println("Student name: ${it.name}")
                println("Student gender: ${it.gender}")
                println("Student address: ${it.address}")
                println("Student birthday: ${it.birthday}")
                println("Student department: ${it.department}")
                println("Student password same as id: ${userService.authenticate(it.id, it.id)}")
                println("Student enrollments:")
                it.enrollments?.forEach { enroll ->
                    println(enroll)
                }
                println("")
            }

            courses.findAll().forEach {
                println("")
                println("Course id: ${it.id}")
                println("Course title: ${it.title}")
                println("Course section: ${it.section}")
                println("Course taught by: ${it.taughtBy}")
                println("Course enrollers: ${it.enrollments?.size ?: "Unknown"}")
                println("")
            }

            staffs.findAll().forEach {
                println("")
                println("Staff id: ${it.id}")
                println("Staff name: ${it.name}")
                println("Staff gender: ${it.gender}")
                println("Staff address: ${it.address}")
                println("Staff birthday: ${it.birthday}")
                println("Staff department: ${it.department}")
                println("Staff password same as id: ${userService.authenticate(it.id, it.id)}")
                println("Staff teachings:")
                it.teachings?.forEach { teaching ->
                    println(teaching)
                }
                println("")
            }
        }.run()
         */

        super.init()
    }
}

fun main(args: Array<String>) {
    Application.launch(AssignmentApplication::class.java, *args)
}