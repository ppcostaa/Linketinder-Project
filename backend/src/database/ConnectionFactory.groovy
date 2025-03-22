package database

import groovy.sql.Sql

import java.sql.Connection
import java.sql.DriverManager

//package database
//import groovy.sql.Sql
//class ConnectionFactory {
//
//    def url = 'jdbc:postgresql://localhost:5432/linketinder'
//    def user = 'postgres'
//    def password = 'senha123'
//    def driver = 'org.postgresql.Driver'
//
//    def obterConexao() {
//        try {
//            def sql = Sql.newInstance(url, user, password, driver)
//            println "✅ Conexão bem-sucedida!"
//
//            sql.eachRow('SELECT * FROM Candidatos') { row ->
//                println "Usuário: ${row.nome}"
//            }
//
//            sql.close()
//        } catch (Exception e) {
//            println "❌ Erro ao conectar ao banco de dados: ${e.message}"
//        }
//    }
//}
//

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
