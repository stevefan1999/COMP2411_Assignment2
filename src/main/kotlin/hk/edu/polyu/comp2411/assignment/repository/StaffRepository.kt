package hk.edu.polyu.comp2411.assignment.repository

import hk.edu.polyu.comp2411.assignment.entity.StaffEntity
import org.springframework.data.repository.CrudRepository

interface StaffRepository : CrudRepository<StaffEntity, String>