package hk.edu.polyu.comp2411.assignment.repository

import hk.edu.polyu.comp2411.assignment.entity.CourseEntity
import org.springframework.data.repository.CrudRepository

interface CourseRepository : CrudRepository<CourseEntity, String>