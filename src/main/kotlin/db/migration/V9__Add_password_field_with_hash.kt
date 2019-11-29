package db.migration

import hk.edu.polyu.comp2411.assignment.extension.bcrypt
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.SingleConnectionDataSource

open class V9__Add_password_field_with_hash : BaseJavaMigration() {
    override fun migrate(context: Context?) {
        if (context !== null) {
            val jdbc = JdbcTemplate(SingleConnectionDataSource(context.connection, true))

            jdbc.execute(
                """
                    alter table USERS
                        add (password VARCHAR(60))
                """
            )

            jdbc.queryForList("SELECT id FROM USERS", String::class.java).forEach {
                jdbc.update("UPDATE users SET password = ? WHERE id = ?", it.bcrypt(), it)
            }

            jdbc.execute(
                """
                alter table USERS
                    modify (password not null)
                """
            )
        }
    }

}
