package groovy.database

import spock.lang.Specification
import spock.lang.Shared
import java.sql.Connection

class DatabaseConnectionTest extends Specification {
    @Shared
    File tempEnvFile

    def setupSpec() {
        tempEnvFile = File.createTempFile("test", ".env")
        tempEnvFile.write("""
            db.url=jdbc:postgresql://localhost:5432/linketinder
            db.user=postgres
            db.pass=senha123
        """.stripIndent())

        EnvReader.metaClass.constructor = { ->
            return new EnvReader(tempEnvFile)
        }
    }

    def cleanupSpec() {
        tempEnvFile.delete()
        EnvReader.metaClass = null
    }

    def "deve criar uma fábrica de conexão corretamente"() {
        when: "uma fábrica de conexão é criada"
        def connectionFactory = DatabaseFactory.createConnectionFactory()

        then: "a fábrica é instanciada corretamente"
        connectionFactory != null
        connectionFactory instanceof ConnectionFactory
        connectionFactory.url == "jdbc:postgresql://localhost:5432/linketinder"
        connectionFactory.username == "postgres"
        connectionFactory.password == "senha123"
    }

    def "deve estabelecer uma conexão com o banco de dados"() {
        given: "uma fábrica de conexão"
        def connectionFactory = DatabaseFactory.createConnectionFactory()

        when: "uma conexão é solicitada"
        Connection connection = connectionFactory.createConnection()

        then: "a conexão é estabelecida com sucesso"
        connection != null
        !connection.isClosed()

        cleanup:
        connection?.close()
    }

    def "deve criar uma instância Sql válida"() {
        given: "uma fábrica de conexão"
        def connectionFactory = DatabaseFactory.createConnectionFactory()

        when: "uma instância Sql é solicitada"
        def sql = connectionFactory.createSql()
        def result = sql.firstRow("SELECT 1 as test")

        then: "a instância Sql é válida e executou uma consulta com sucesso"
        result?.test == 1

        cleanup:
        sql?.close()
    }

    def "deve lançar exceção quando não conseguir conectar ao banco"() {
        given: "uma configuração inválida de banco de dados"
        tempEnvFile.write("""
            db.url=jdbc:postgresql://localhost:5432/banco_inexistente
            db.user=usuario_invalido
            db.pass=senha_invalida
        """.stripIndent())

        when: "tenta criar uma conexão"
        DatabaseFactory.createConnectionFactory().createConnection()

        then: "uma exceção é lançada"
        thrown(RuntimeException)
    }
}