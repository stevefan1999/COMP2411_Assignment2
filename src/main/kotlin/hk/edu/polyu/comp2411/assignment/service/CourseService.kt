package hk.edu.polyu.comp2411.assignment.service

import hk.edu.polyu.comp2411.assignment.entity.CourseEntity
import hk.edu.polyu.comp2411.assignment.repository.CourseRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CourseService(
    var courses: CourseRepository
) {
    fun addCourse(partialCourse: CourseEntity): Boolean = when {
        courses.findByIdOrNull(partialCourse.id) == null -> courses.save(partialCourse) == partialCourse
        else -> false
    }


}