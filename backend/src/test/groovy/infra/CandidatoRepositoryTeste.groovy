package infra

import groovy.database.ConnectionFactory
import groovy.infra.CandidatoRepository
import groovy.model.Candidato
import groovy.model.Competencia
import spock.lang.Specification
import spock.lang.Subject

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.text.SimpleDateFormat

class CandidatoRepositoryTeste extends Specification {

    @Subject
    CandidatoRepository candidatoRepository

    ConnectionFactory connectionFactory
    Connection connection
    PreparedStatement preparedStatement
    ResultSet resultSet

    def setup() {
        connectionFactory = Mock(ConnectionFactory)
        connection = Mock(Connection)
        preparedStatement = Mock(PreparedStatement)
        resultSet = Mock(ResultSet)

        candidatoRepository = new CandidatoRepository()
        candidatoRepository.connectionFactory = connectionFactory
    }

    def "deve salvar um candidato com sucesso"() {
        given: "um candidato para ser salvo"
        def sdf = new SimpleDateFormat("dd/MM/yyyy")
        def candidato = new Candidato(
                nome: "Test",
                sobrenome: "Candidate",
                cpf: "12345678901",
                dataNascimento: sdf.parse("01/01/1990"),
                competencias: [new Competencia(competenciaId: 1)]
        )
        def userResultSet = Mock(ResultSet)
        def locResultSet = Mock(ResultSet)
        def candResultSet = Mock(ResultSet)
        def competenciaStmt = Mock(PreparedStatement)

        when: "o método salvarCandidato é chamado"
        def resultado = candidatoRepository.salvarCandidato(candidato, "test@test.com", "pass", "desc", "12345", "BR")

        then: "a conexão com o banco é estabelecida e configurada"
        1 * connectionFactory.createConnection() >> connection
        1 * connection.setAutoCommit(false)

        and: "o usuário é inserido no banco"
        1 * connection.prepareStatement("INSERT INTO USUARIOS (EMAIL, SENHA, DESCRICAO) VALUES (?, ?, ?) RETURNING ID_USUARIO",
                Statement.RETURN_GENERATED_KEYS) >> preparedStatement
        1 * preparedStatement.setString(1, "test@test.com")
        1 * preparedStatement.setString(2, "pass")
        1 * preparedStatement.setString(3, "desc")
        1 * preparedStatement.executeUpdate() >> 1
        1 * preparedStatement.getGeneratedKeys() >> userResultSet
        1 * userResultSet.next() >> true
        1 * userResultSet.getInt(1) >> 1
        1 * userResultSet.close()

        and: "a localização é inserida no banco"
        1 * connection.prepareStatement("INSERT INTO LOCALIZACAO (CEP, PAIS) VALUES (?, ?) RETURNING ID_LOCALIZACAO",
                Statement.RETURN_GENERATED_KEYS) >> preparedStatement
        1 * preparedStatement.setString(1, "12345")
        1 * preparedStatement.setString(2, "BR")
        1 * preparedStatement.executeUpdate() >> 1
        1 * preparedStatement.getGeneratedKeys() >> locResultSet
        1 * locResultSet.next() >> true
        1 * locResultSet.getInt(1) >> 1
        1 * locResultSet.close()

        and: "o candidato é inserido no banco"
        1 * connection.prepareStatement("""
            INSERT INTO CANDIDATOS (ID_USUARIO, ID_LOCALIZACAO, NOME, SOBRENOME, DATA_NASCIMENTO, CPF)
            VALUES (?, ?, ?, ?, ?, ?) RETURNING ID_CANDIDATO
        """, Statement.RETURN_GENERATED_KEYS) >> preparedStatement
        1 * preparedStatement.setInt(1, 1)
        1 * preparedStatement.setInt(2, 1)
        1 * preparedStatement.setString(3, "Test")
        1 * preparedStatement.setString(4, "Candidate")
        1 * preparedStatement.setDate(5, _ as java.sql.Date)
        1 * preparedStatement.setString(6, "12345678901")
        1 * preparedStatement.executeUpdate() >> 1
        1 * preparedStatement.getGeneratedKeys() >> candResultSet
        1 * candResultSet.next() >> true
        1 * candResultSet.getInt(1) >> 1
        1 * candResultSet.close()

        and: "as competências são associadas ao candidato"
        1 * connection.prepareStatement("""
                    INSERT INTO CANDIDATO_COMPETENCIAS (ID_CANDIDATO, ID_COMPETENCIA)
                    VALUES (?, ?)
                """) >> competenciaStmt
        1 * competenciaStmt.setInt(1, 1)
        1 * competenciaStmt.setInt(2, 1)
        1 * competenciaStmt.executeUpdate() >> 1
        1 * competenciaStmt.close()

        and: "a transação é finalizada"
        1 * connection.commit()
        1 * connection.setAutoCommit(true)
        1 * connection.close()

        and: "o candidato é retornado com id"
        resultado == candidato
        candidato.candidatoId == 1
    }

    def "deve listar um candidato por ID"() {
        given: "um id de candidato"
        def candidatoId = 1

        when: "o método listarCandidatoPorId é chamado"
        def candidato = candidatoRepository.listarCandidatoPorId(candidatoId)

        then: "a conexão com o banco é estabelecida"
        1 * connectionFactory.createConnection() >> connection
        1 * connection.prepareStatement(_ as String) >> preparedStatement
        1 * preparedStatement.setInt(1, candidatoId)
        1 * preparedStatement.executeQuery() >> resultSet

        and: "os dados do candidato são recuperados"
        1 * resultSet.next() >> true
        1 * resultSet.getInt("ID_CANDIDATO") >> 1
        1 * resultSet.getInt("ID_USUARIO") >> 2
        1 * resultSet.getInt("ID_LOCALIZACAO") >> 3
        1 * resultSet.getString("NOME") >> "Test"
        1 * resultSet.getString("SOBRENOME") >> "Candidate"
        1 * resultSet.getDate("DATA_NASCIMENTO") >> new java.sql.Date(0)
        1 * resultSet.getString("CPF") >> "12345678901"

        and: "o candidato é retornado corretamente"
        candidato != null
        candidato.candidatoId == 1
        candidato.nome == "Test"
        candidato.sobrenome == "Candidate"
        candidato.cpf == "12345678901"
    }

    def "deve excluir um candidato com sucesso"() {
        given: "um id de candidato"
        def candidatoId = 1

        when: "o método excluirCandidato é chamado"
        def resultado = candidatoRepository.excluirCandidato(candidatoId)

        then: "a conexão com o banco é estabelecida e configurada"
        1 * connectionFactory.createConnection() >> connection
        1 * connection.setAutoCommit(false)

        and: "as competências do candidato são excluídas"
        1 * connection.prepareStatement("DELETE FROM CANDIDATO_COMPETENCIAS WHERE ID_CANDIDATO = ?") >> preparedStatement
        1 * preparedStatement.setInt(1, candidatoId)
        1 * preparedStatement.executeUpdate() >> 1
        1 * preparedStatement.close()

        and: "o ID do usuário é recuperado"
        1 * connection.prepareStatement("SELECT ID_USUARIO FROM CANDIDATOS WHERE ID_CANDIDATO = ?") >> preparedStatement
        1 * preparedStatement.setInt(1, candidatoId)
        1 * preparedStatement.executeQuery() >> resultSet
        1 * resultSet.next() >> true
        1 * resultSet.getInt("ID_USUARIO") >> 1
        1 * resultSet.close()
        1 * preparedStatement.close()

        and: "o candidato é excluído"
        1 * connection.prepareStatement("DELETE FROM CANDIDATOS WHERE ID_CANDIDATO = ?") >> preparedStatement
        1 * preparedStatement.setInt(1, candidatoId)
        1 * preparedStatement.executeUpdate() >> 1
        1 * preparedStatement.close()

        and: "o usuário associado é excluído"
        1 * connection.prepareStatement("DELETE FROM USUARIOS WHERE ID_USUARIO = ?") >> preparedStatement
        1 * preparedStatement.setInt(1, 1)
        1 * preparedStatement.executeUpdate() >> 1
        1 * preparedStatement.close()

        and: "a transação é finalizada"
        1 * connection.commit()
        1 * connection.setAutoCommit(true)
        1 * connection.close()

        and: "o resultado é verdadeiro"
        resultado == true
    }

    def "deve editar um candidato com sucesso"() {
        given: "um candidato para ser editado"
        def sdf = new SimpleDateFormat("dd/MM/yyyy")
        def candidato = new Candidato(
                candidatoId: 1,
                nome: "Test Updated",
                sobrenome: "Candidate Updated",
                cpf: "12345678901",
                dataNascimento: sdf.parse("01/01/1990"),
                competencias: [new Competencia(competenciaId: 2)]
        )
        def competenciaStmt = Mock(PreparedStatement)

        when: "o método editarCandidato é chamado"
        def resultado = candidatoRepository.editarCandidato(candidato)

        then: "a conexão com o banco é estabelecida e configurada"
        1 * connectionFactory.createConnection() >> connection
        1 * connection.setAutoCommit(false)

        and: "os dados do candidato são atualizados"
        1 * connection.prepareStatement("UPDATE CANDIDATOS SET NOME = ?, SOBRENOME = ?, DATA_NASCIMENTO = ?, CPF = ? WHERE ID_CANDIDATO = ?") >> preparedStatement
        1 * preparedStatement.setString(1, "Test Updated")
        1 * preparedStatement.setString(2, "Candidate Updated")
        1 * preparedStatement.setDate(3, _ as java.sql.Date)
        1 * preparedStatement.setString(4, "12345678901")
        1 * preparedStatement.setInt(5, 1)
        1 * preparedStatement.executeUpdate() >> 1

        and: "as competências antigas são excluídas"
        1 * connection.prepareStatement("DELETE FROM CANDIDATO_COMPETENCIAS WHERE ID_CANDIDATO = ?") >> preparedStatement
        1 * preparedStatement.setInt(1, 1)
        1 * preparedStatement.executeUpdate() >> 1
        1 * preparedStatement.close()

        and: "as novas competências são associadas"
        1 * connection.prepareStatement("INSERT INTO CANDIDATO_COMPETENCIAS (ID_CANDIDATO, ID_COMPETENCIA) VALUES (?, ?)") >> competenciaStmt
        1 * competenciaStmt.setInt(1, 1)
        1 * competenciaStmt.setInt(2, 2)
        1 * competenciaStmt.executeUpdate() >> 1
        1 * competenciaStmt.close()

        and: "a transação é finalizada"
        1 * connection.commit()
        1 * connection.setAutoCommit(true)
        1 * connection.close()

        and: "o resultado é verdadeiro"
        resultado == true
    }
}