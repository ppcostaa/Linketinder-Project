package database

import java.sql.Connection

interface IConnectionFactory {
    Connection createConnection()
}