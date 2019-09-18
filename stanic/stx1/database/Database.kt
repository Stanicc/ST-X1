package stanic.stx1.database

import java.sql.Connection
import java.sql.Statement

interface Database {

    val connection: Connection?
    val statement: Statement?

    val type: String

    fun open(): Boolean
    fun close(): Boolean

    fun connection(): Boolean

}