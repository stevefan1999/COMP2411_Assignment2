package hk.edu.polyu.comp2411.assignment.repository

import hk.edu.polyu.comp2411.assignment.entity.EnrollmentEntity
import org.springframework.data.repository.CrudRepository

interface EnrollmentRepository : CrudRepository<EnrollmentEntity, String>