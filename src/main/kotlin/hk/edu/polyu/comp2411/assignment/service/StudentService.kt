package hk.edu.polyu.comp2411.assignment.service

import hk.edu.polyu.comp2411.assignment.entity.StudentEntity
import hk.edu.polyu.comp2411.assignment.entity.UserBaseEntity
import hk.edu.polyu.comp2411.assignment.extension.bcryptCheck
import hk.edu.polyu.comp2411.assignment.repository.StudentRepository
import hk.edu.polyu.comp2411.assignment.repository.UserBaseRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import tornadofx.information

@Service
class StudentService(
    var students: StudentRepository
) {
    fun addStudent(partialStudent: StudentEntity): StudentEntity? {
        partialStudent.id = findSuitableId()

        return when (students.save(partialStudent)) {
            partialStudent -> partialStudent
            else -> null
        }
    }

    fun findSuitableId(): String {
        val id = "S${"${(0..999999).random()}".padStart(7, '0')}"
        return if (students.findByIdOrNull(id) == null) id else findSuitableId()
    }
}