package interfaces

import java.sql.Connection

interface IConnectionFactory {
    Connection createConnection()
}