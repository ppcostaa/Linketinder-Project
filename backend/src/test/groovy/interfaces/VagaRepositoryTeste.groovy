package interfaces

import database.ConnectionFactory
import model.Competencia
import model.Vaga
import repository.CompetenciaRepository
import repository.VagaRepository
import spock.lang.Specification
import spock.lang.Subject

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

class VagaRepositoryTeste extends Specification {

    @Subject
    VagaRepository vagaRepository

    ConnectionFactory connectionFactory
    Connection connection
    PreparedStatement preparedStatement
    ResultSet resultSet

    def setup() {
        connectionFactory = Mock(ConnectionFactory)
        connection = Mock(Connection)
        preparedStatement = Mock(PreparedStatement)
        resultSet = Mock(ResultSet)

        vagaRepository = new VagaRepository()
        vagaRepository.connectionFactory = connectionFactory
    }

    def "deve salvar uma vaga com sucesso"() {
        given: "uma vaga para ser salva"
        def vaga = new Vaga(
                empresaId: 1,
                titulo: "Desenvolvedor Java",
                descricao: "Vaga para desenvolvedor Java",
                estado: "SP",
                cidade: "São Paulo",
                competencias: [new Competencia(competenciaId: 1)]
        )
        def vagaResultSet = Mock(ResultSet)
        def competenciaStmt = Mock(PreparedStatement)

        when: "o método salvarVaga é chamado"
        def resultado = vagaRepository.salvarVaga(vaga)

        then: "a conexão com o banco é estabelecida e configurada"
        1 * connectionFactory.createConnection() >> connection
        1 * connection.setAutoCommit(false)

        and: "a vaga é inserida no banco"
        1 * connection.prepareStatement("INSERT INTO VAGAS (ID_EMPRESA, NOME_VAGA, DESCRICAO_VAGA, LOCAL_ESTADO, LOCAL_CIDADE) VALUES (?, ?, ?, ?, ?) RETURNING ID_VAGA",
                Statement.RETURN_GENERATED_KEYS) >> preparedStatement
        1 * preparedStatement.setInt(1, 1)
        1 * preparedStatement.setString(2, "Desenvolvedor Java")
        1 * preparedStatement.setString(3, "Vaga para desenvolvedor Java")
        1 * preparedStatement.setString(4, "SP")
        1 * preparedStatement.setString(5, "São Paulo")
        1 * preparedStatement.executeUpdate() >> 1
        1 * preparedStatement.getGeneratedKeys() >> vagaResultSet
        1 * vagaResultSet.next() >> true
        1 * vagaResultSet.getInt(1) >> 1
        1 * vagaResultSet.close()

        and: "as competências são associadas à vaga"
        1 * connection.prepareStatement("INSERT INTO VAGA_COMPETENCIAS (ID_VAGA, ID_COMPETENCIA) VALUES (?, ?)") >> competenciaStmt
        1 * competenciaStmt.setInt(1, 1)
        1 * competenciaStmt.setInt(2, 1)
        1 * competenciaStmt.executeUpdate() >> 1
        1 * competenciaStmt.close()

        and: "a transação é finalizada"
        1 * connection.commit()
        1 * connection.setAutoCommit(true)
        1 * connection.close()

        and: "a vaga é retornada com id"
        resultado == vaga
        vaga.vagaId == 1
    }

    def "deve listar uma vaga por ID"() {
        given: "um id de vaga"
        def vagaId = 1

        when: "o método listarVagaPorId é chamado"
        def vaga = vagaRepository.listarVagaPorId(vagaId)

        then: "a conexão com o banco é estabelecida"
        1 * connectionFactory.createConnection() >> connection
        1 * connection.prepareStatement(_ as String) >> preparedStatement
        1 * preparedStatement.setInt(1, vagaId)
        1 * preparedStatement.executeQuery() >> resultSet

        and: "os dados da vaga são recuperados"
        1 * resultSet.next() >> true
        1 * resultSet.getInt("ID_VAGA") >> 1
        1 * resultSet.getInt("ID_EMPRESA") >> 1
        1 * resultSet.getString("NOME_VAGA") >> "Desenvolvedor Java"
        1 * resultSet.getString("DESCRICAO_VAGA") >> "Vaga para desenvolvedor Java"
        1 * resultSet.getString("LOCAL_ESTADO") >> "SP"
        1 * resultSet.getString("LOCAL_CIDADE") >> "São Paulo"

        and: "as competências da vaga são recuperadas"
        1 * connection.prepareStatement("SELECT C.ID_COMPETENCIA, C.NOME_COMPETENCIA FROM COMPETENCIAS C JOIN VAGA_COMPETENCIAS VC ON C.ID_COMPETENCIA = VC.ID_COMPETENCIA WHERE VC.ID_VAGA = ?") >> preparedStatement
        1 * preparedStatement.setInt(1, 1)
        1 * preparedStatement.executeQuery() >> resultSet
        2 * resultSet.next() >>> [true, false]
        1 * resultSet.getInt("ID_COMPETENCIA") >> 1
        1 * resultSet.getString("NOME_COMPETENCIA") >> "Java"

        and: "a vaga é retornada corretamente"
        vaga != null
        vaga.vagaId == 1
        vaga.titulo == "Desenvolvedor Java"
        vaga.competencias.size() == 1
        vaga.competencias[0].competenciaId == 1
    }

    def "deve excluir uma vaga com sucesso"() {
        given: "um id de vaga"
        def vagaId = 1

        when: "o método excluirVaga é chamado"
        def resultado = vagaRepository.excluirVaga(vagaId)

        then: "a conexão com o banco é estabelecida e configurada"
        1 * connectionFactory.createConnection() >> connection
        1 * connection.setAutoCommit(false)

        and: "as competências da vaga são excluídas"
        1 * connection.prepareStatement("DELETE FROM VAGA_COMPETENCIAS WHERE ID_VAGA = ?") >> preparedStatement
        1 * preparedStatement.setInt(1, vagaId)
        1 * preparedStatement.executeUpdate() >> 1
        1 * preparedStatement.close()

        and: "a vaga é excluída"
        1 * connection.prepareStatement("DELETE FROM VAGAS WHERE ID_VAGA = ?") >> preparedStatement
        1 * preparedStatement.setInt(1, vagaId)
        1 * preparedStatement.executeUpdate() >> 1
        1 * preparedStatement.close()

        and: "a transação é finalizada"
        1 * connection.commit()
        1 * connection.setAutoCommit(true)
        1 * connection.close()

        and: "o resultado é verdadeiro"
        resultado == true
    }

    def "deve editar uma vaga com sucesso"() {
        given: "uma vaga para ser editada"
        def vaga = new Vaga(
                vagaId: 1,
                empresaId: 1,
                titulo: "Desenvolvedor Java Atualizado",
                descricao: "Vaga atualizada para desenvolvedor Java",
                estado: "RJ",
                cidade: "Rio de Janeiro",
                competencias: [new Competencia(competenciaId: 2)]
        )
        def competenciaStmt = Mock(PreparedStatement)

        when: "o método editarVaga é chamado"
        def resultado = vagaRepository.editarVaga(vaga)

        then: "a conexão com o banco é estabelecida"
        1 * connectionFactory.createConnection() >> connection

        and: "os dados da vaga são atualizados"
        1 * connection.prepareStatement("UPDATE VAGAS SET NOME_VAGA = ?, DESCRICAO_VAGA = ?, LOCAL_ESTADO = ?, LOCAL_CIDADE = ? WHERE ID_VAGA = ?") >> preparedStatement
        1 * preparedStatement.setString(1, "Desenvolvedor Java Atualizado")
        1 * preparedStatement.setString(2, "Vaga atualizada para desenvolvedor Java")
        1 * preparedStatement.setString(3, "RJ")
        1 * preparedStatement.setString(4, "Rio de Janeiro")
        1 * preparedStatement.setInt(5, 1)
        1 * preparedStatement.executeUpdate() >> 1

        and: "o resultado é verdadeiro"
        resultado == true
    }

    def "deve listar todas as vagas"() {
        given: "várias vagas no banco de dados"
        def vagaResultSet = Mock(ResultSet)
        def competenciaRepository = Mock(CompetenciaRepository)
        vagaRepository.competenciaRepository = competenciaRepository

        when: "o método listarVagas é chamado"
        def vagas = vagaRepository.listarVagas()

        then: "a conexão com o banco é estabelecida"
        1 * connectionFactory.createConnection() >> connection

        and: "as vagas são recuperadas"
        1 * connection.prepareStatement("SELECT * FROM VAGAS") >> preparedStatement
        1 * preparedStatement.executeQuery() >> vagaResultSet
        2 * vagaResultSet.next() >>> [true, false]
        1 * vagaResultSet.getInt("ID_VAGA") >> 1
        1 * vagaResultSet.getInt("ID_EMPRESA") >> 1
        1 * vagaResultSet.getString("NOME_VAGA") >> "Desenvolvedor Java"
        1 * vagaResultSet.getString("DESCRICAO_VAGA") >> "Vaga para desenvolvedor Java"
        1 * vagaResultSet.getString("LOCAL_ESTADO") >> "SP"
        1 * vagaResultSet.getString("LOCAL_CIDADE") >> "São Paulo"

        and: "as competências são buscadas pelo repositório"
        1 * competenciaRepository.listarCompetenciasPorVaga(1) >> [new Competencia(competenciaId: 1, competenciaNome: "Java")]

        and: "a lista de vagas é retornada"
        vagas.size() == 1
        vagas[0].vagaId == 1
        vagas[0].competencias.size() == 1
        vagas[0].competencias[0].competenciaId == 1
    }
}