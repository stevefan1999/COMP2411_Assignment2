package hk.edu.polyu.comp2411.assignment.repository

import hk.edu.polyu.comp2411.assignment.entity.UserBaseEntity
import org.springframework.data.repository.CrudRepository

interface UserBaseRepository : CrudRepository<UserBaseEntity, String>