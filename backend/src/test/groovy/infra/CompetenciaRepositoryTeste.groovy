package groovy.infra

import groovy.database.ConnectionFactory
import groovy.model.Competencia
import spock.lang.Specification
import spock.lang.Subject

import java.sql.*

class CompetenciaRepositoryTeste extends Specification {

    @Subject
    CompetenciaRepository repository

    ConnectionFactory connectionFactory = Mock()
    Connection connection = Mock()
    PreparedStatement preparedStatement = Mock()
    ResultSet resultSet = Mock()

    def setup() {
        repository = new CompetenciaRepository()
        repository.connectionFactory = connectionFactory
    }

    def "deve salvar uma competência com sucesso"() {
        given: "uma competência válida"
        def competencia = new Competencia(competenciaNome: "Java")

        and: "mocks configurados completamente"
        connectionFactory.createConnection() >> connection
        connection.setAutoCommit(false) >> null

        connection.prepareStatement(_, Statement.RETURN_GENERATED_KEYS) >> preparedStatement
        preparedStatement.setString(1, "Java") >> null
        preparedStatement.executeUpdate() >> 1

        preparedStatement.getGeneratedKeys() >> resultSet
        resultSet.next() >> true
        resultSet.getInt(1) >> 123

        when: "tenta salvar a competência"
        def resultado = repository.salvarCompetencia(competencia)

        then: "a competência é salva com ID gerado"
        resultado.competenciaId == 123
        resultado.competenciaNome == "Java"

        and: "verificações de interações"
        1 * connection.commit()
        1 * connection.setAutoCommit(true)
        1 * connection.close()
    }

    def "deve lançar exceção e fazer rollback ao falhar ao salvar competência"() {
        given: "uma competência válida"
        def competencia = new Competencia(competenciaNome: "Java")

        and: "mocks configurados para simular falha"
        connectionFactory.createConnection() >> connection
        connection.setAutoCommit(false) >> null

        connection.prepareStatement(_, Statement.RETURN_GENERATED_KEYS) >> preparedStatement
        preparedStatement.setString(1, "Java") >> { throw new SQLException("Erro de banco") }

        when: "tenta salvar a competência"
        repository.salvarCompetencia(competencia)

        then: "uma exceção é lançada"
        thrown(RuntimeException)

        and: "verificações de rollback e fechamento"
        1 * connection.rollback()
        1 * connection.setAutoCommit(true)
        1 * connection.close()
    }


    def "deve listar competências por candidato com sucesso"() {
        given: "um ID de candidato válido"
        def candidatoId = 1

        and: "mocks configurados"
        connectionFactory.createConnection() >> connection
        connection.prepareStatement(_ as String) >> preparedStatement
        preparedStatement.executeQuery() >> resultSet
        resultSet.next() >>> [true, true, false]
        resultSet.getInt("ID_COMPETENCIA") >>> [1, 2]
        resultSet.getString("NOME_COMPETENCIA") >>> ["Java", "Groovy"]

        when: "lista competências por candidato"
        def competencias = repository.listarCompetenciasPorCandidato(candidatoId)

        then: "retorna lista com competências esperadas"
        competencias.size() == 2
        competencias[0].competenciaId == 1
        competencias[0].competenciaNome == "Java"
        competencias[1].competenciaId == 2
        competencias[1].competenciaNome == "Groovy"
    }

    def "deve listar todas as competências com sucesso"() {
        given: "mocks configurados"
        connectionFactory.createConnection() >> connection
        connection.prepareStatement(_ as String) >> preparedStatement
        preparedStatement.executeQuery() >> resultSet
        resultSet.next() >>> [true, true, false]
        resultSet.getInt("ID_COMPETENCIA") >>> [1, 2]
        resultSet.getString("NOME_COMPETENCIA") >>> ["Java", "Groovy"]

        when: "lista todas as competências"
        def competencias = repository.listarCompetencias()

        then: "retorna lista com todas competências"
        competencias.size() == 2
        competencias[0].competenciaId == 1
        competencias[0].competenciaNome == "Java"
        competencias[1].competenciaId == 2
        competencias[1].competenciaNome == "Groovy"
    }

    def "deve listar competências por vaga com sucesso"() {
        given: "um ID de vaga válido"
        def vagaId = 1

        and: "mocks configurados"
        connectionFactory.createConnection() >> connection
        connection.prepareStatement(_ as String) >> preparedStatement
        preparedStatement.executeQuery() >> resultSet
        resultSet.next() >>> [true, true, false]
        resultSet.getInt("ID_COMPETENCIA") >>> [1, 2]
        resultSet.getString("NOME_COMPETENCIA") >>> ["Java", "Groovy"]

        when: "lista competências por vaga"
        def competencias = repository.listarCompetenciasPorVaga(vagaId)

        then: "retorna lista com competências esperadas"
        competencias.size() == 2
        competencias[0].competenciaId == 1
        competencias[0].competenciaNome == "Java"
        competencias[1].competenciaId == 2
        competencias[1].competenciaNome == "Groovy"
    }

    def "deve editar competência com sucesso"() {
        given: "uma competência válida para edição"
        def competencia = new Competencia(competenciaId: 1, competenciaNome: "Java Atualizado")

        and: "mocks configurados"
        connectionFactory.createConnection() >> connection
        connection.prepareStatement(_ as String) >> preparedStatement
        preparedStatement.executeUpdate() >> 1

        when: "edita a competência"
        def resultado = repository.editarCompetencia(competencia)

        then: "retorna true indicando sucesso"
        resultado

        and: "commit é chamado"
        1 * connection.commit()
    }

    def "deve retornar competência ao buscar por ID existente"() {
        given: "um ID de competência existente"
        def competenciaId = 1

        and: "mocks configurados"
        connectionFactory.createConnection() >> connection
        connection.prepareStatement(_ as String) >> preparedStatement
        preparedStatement.executeQuery() >> resultSet
        resultSet.next() >> true
        resultSet.getInt("ID_COMPETENCIA") >> 1
        resultSet.getString("NOME_COMPETENCIA") >> "Java"

        when: "busca competência por ID"
        def competencia = repository.listarCompetenciasPorId(competenciaId)

        then: "retorna a competência esperada"
        competencia.competenciaId == 1
        competencia.competenciaNome == "Java"
    }

    def "deve retornar null ao buscar por ID inexistente"() {
        given: "um ID de competência inexistente"
        def competenciaId = 999

        and: "mocks configurados"
        connectionFactory.createConnection() >> connection
        connection.prepareStatement(_ as String) >> preparedStatement
        preparedStatement.executeQuery() >> resultSet
        resultSet.next() >> false

        when: "busca competência por ID"
        def competencia = repository.listarCompetenciasPorId(competenciaId)

        then: "retorna null"
        competencia == null
    }

    def "deve excluir competência com sucesso"() {
        given: "um ID de competência existente"
        def competenciaId = 1

        and: "mocks configurados"
        connectionFactory.createConnection() >> connection
        connection.prepareStatement(_ as String) >> preparedStatement
        preparedStatement.executeUpdate() >> 1

        when: "exclui a competência"
        def resultado = repository.excluirCompetencia(competenciaId)

        then: "retorna true indicando sucesso"
        resultado
    }

    def "deve retornar false ao tentar excluir competência inexistente"() {
        given: "um ID de competência inexistente"
        def competenciaId = 999

        and: "mocks configurados"
        connectionFactory.createConnection() >> connection
        connection.prepareStatement(_ as String) >> preparedStatement
        preparedStatement.executeUpdate() >> 0

        when: "exclui a competência"
        def resultado = repository.excluirCompetencia(competenciaId)

        then: "retorna false indicando falha"
        !resultado
    }
}