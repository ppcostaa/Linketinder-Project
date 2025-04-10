package database

import groovy.sql.Sql

import java.sql.Connection
import java.sql.DriverManager

class ConnectionFactory implements IConnectionFactory {
    final String url
    final String username
    final String password

    ConnectionFactory(String url, String username, String password) {
        this.url = url
        this.username = username
        this.password = password
    }

    @Override
    Connection createConnection() {
        try {
            Class.forName("org.postgresql.Driver")
            return DriverManager.getConnection(url, username, password)
        } catch (Exception e) {
            println "Erro ao criar conexão: ${e.message}"
            throw new RuntimeException("Falha ao criar conexão com o banco de dados", e)
        }
    }

    Sql createSql() {
        try {
            return Sql.newInstance(url, username, password)
        } catch (Exception e) {
            println "Erro ao criar conexão Sql: ${e.message}"
            throw new RuntimeException("Falha ao criar conexão Sql com o banco de dados", e)
        }
    }
}
