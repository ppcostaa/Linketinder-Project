package DAO

import database.ConnectionFactory
import model.Localizacao
import DAO.LocalizacaoDAO
import spock.lang.Specification
import spock.lang.Subject

import java.sql.*

class LocalizacaoDAOTeste extends Specification {

    @Subject
    LocalizacaoDAO repository

    ConnectionFactory connectionFactory = Mock()
    Connection connection = Mock()
    PreparedStatement preparedStatement = Mock()
    ResultSet resultSet = Mock()

    def setup() {
        repository = new LocalizacaoDAO(connectionFactory)
    }

    def "deve retornar localizacao ao buscar por ID existente"() {
        given: "um ID de localizacao existente"
        def localizacaoId = 1

        and: "mocks configurados"
        connectionFactory.createConnection() >> connection
        connection.prepareStatement(_ as String) >> preparedStatement
        preparedStatement.executeQuery() >> resultSet
        resultSet.next() >> true
        resultSet.getInt("ID_LOCALIZACAO") >> 1
        resultSet.getString("CEP") >> "12345-678"
        resultSet.getString("PAIS") >> "Brasil"

        when: "busca localizacao por ID"
        def localizacao = repository.listarLocalizacoesPorId(localizacaoId)

        then: "retorna a localizacao esperada"
        localizacao.localizacaoId == 1
        localizacao.cep == "12345-678"
        localizacao.pais == "Brasil"
    }

    def "deve retornar null ao buscar por ID inexistente"() {
        given: "um ID de localizacao inexistente"
        def localizacaoId = 999

        and: "mocks configurados"
        connectionFactory.createConnection() >> connection
        connection.prepareStatement(_ as String) >> preparedStatement
        preparedStatement.executeQuery() >> resultSet
        resultSet.next() >> false

        when: "busca localizacao por ID"
        def localizacao = repository.listarLocalizacoesPorId(localizacaoId)

        then: "retorna null"
        localizacao == null
    }

    def "deve editar localizacao com sucesso"() {
        given: "uma localizacao válida para edição"
        def localizacao = new Localizacao(cep: "12345-678", pais: "Brasil")
        def localizacaoId = 1

        and: "mocks configurados"
        connectionFactory.createConnection() >> connection
        connection.setAutoCommit(false) >> null
        connection.prepareStatement(_ as String) >> preparedStatement
        preparedStatement.setString(1, "12345-678") >> null
        preparedStatement.setString(2, "Brasil") >> null
        preparedStatement.setInt(3, 1) >> null
        preparedStatement.executeUpdate() >> 1

        when: "edita a localizacao"
        def resultado = repository.editarLocalizacao(localizacao, localizacaoId)

        then: "retorna true indicando sucesso"
        resultado

        and: "commit é chamado"
        1 * connection.commit()
        1 * connection.setAutoCommit(true)
        1 * connection.close()
    }

    def "deve retornar false ao tentar editar localizacao inexistente"() {
        given: "uma localizacao com ID inexistente"
        def localizacao = new Localizacao(localizacaoId: 999, cep: "12345-678", pais: "Brasil")

        and: "mocks configurados"
        connectionFactory.createConnection() >> connection
        connection.setAutoCommit(false) >> null
        connection.prepareStatement(_ as String) >> preparedStatement
        preparedStatement.setString(1, "12345-678") >> null
        preparedStatement.setString(2, "Brasil") >> null
        preparedStatement.setInt(3, 999) >> null
        preparedStatement.executeUpdate() >> 0

        when: "edita a localizacao"
        def resultado = repository.editarLocalizacao(localizacao, 999)

        then: "retorna false indicando falha"
        !resultado

        and: "commit é chamado mesmo assim"
        1 * connection.commit()
        1 * connection.setAutoCommit(true)
        1 * connection.close()
    }

    def "deve lançar exceção e fazer rollback ao falhar ao editar localizacao"() {
        given: "uma localizacao válida"
        def localizacao = new Localizacao(localizacaoId: 1, cep: "12345-678", pais: "Brasil")

        and: "mocks configurados para simular falha"
        connectionFactory.createConnection() >> connection
        connection.setAutoCommit(false) >> null
        connection.prepareStatement(_ as String) >> preparedStatement
        preparedStatement.setString(1, "12345-678") >> { throw new SQLException("Erro de banco") }

        when: "tenta editar a localizacao"
        repository.editarLocalizacao(localizacao, 1)

        then: "uma exceção é lançada"
        thrown(RuntimeException)

        and: "verificações de rollback e fechamento"
        1 * connection.rollback()
        1 * connection.setAutoCommit(true)
        1 * connection.close()
    }
}