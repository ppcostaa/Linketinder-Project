package DAO

import database.ConnectionFactory
import model.Empresa
import DAO.EmpresaDAO
import spock.lang.Specification

import java.sql.*

class EmpresaDAOTeste extends Specification {

    EmpresaDAO empresaRepository
    ConnectionFactory connectionFactory
    Connection mockConnection
    PreparedStatement mockPreparedStatement
    ResultSet mockResultSet

    def setup() {
        connectionFactory = Mock(ConnectionFactory)
        mockConnection = Mock(Connection)
        mockPreparedStatement = Mock(PreparedStatement)
        mockResultSet = Mock(ResultSet)

        connectionFactory.createConnection() >> mockConnection
        empresaRepository = new EmpresaDAO(connectionFactory: connectionFactory)
    }

    def "deve salvar uma empresa com sucesso"() {
        given: "uma empresa para salvar"
        def empresa = new Empresa(
                empresaNome: "Empresa Teste",
                cnpj: "12345678901234"
        )
        def email = "teste@empresa.com"
        def senha = "senha123"
        def descricao = "Descrição da empresa"
        def cep = "12345678"
        def pais = "Brasil"

        and: "configuração dos mocks para o fluxo de inserção"
        mockConnection.prepareStatement(_, Statement.RETURN_GENERATED_KEYS) >>> [
                mockPreparedStatement,
                mockPreparedStatement,
                mockPreparedStatement
        ]
        mockPreparedStatement.getGeneratedKeys() >>> [
                mockResultSet,
                mockResultSet,
                mockResultSet
        ]
        mockResultSet.next() >>> [true, true, true]
        mockResultSet.getInt(1) >>> [1, 2, 3]
        mockPreparedStatement.executeUpdate() >>> [1, 1, 1]

        when: "o método salvarEmpresa é chamado"
        def resultado = empresaRepository.salvarEmpresa(empresa, email, senha, descricao, cep, pais)

        then: "a empresa deve ser retornada com ID preenchido"
        resultado.empresaId == 3
        resultado.empresaNome == "Empresa Teste"
        resultado.cnpj == "12345678901234"

        and: "as operações de banco de dados foram chamadas corretamente"
        1 * mockConnection.setAutoCommit(false)
        1 * mockConnection.commit()
        1 * mockConnection.setAutoCommit(true)
        1 * mockConnection.close()
    }

    def "deve listar empresa por ID com sucesso"() {
        given: "um ID de empresa para buscar"
        def empresaId = 1

        and: "configuração dos mocks para busca"
        mockConnection.prepareStatement(_) >> mockPreparedStatement
        mockPreparedStatement.executeQuery() >> mockResultSet
        mockResultSet.next() >> true
        mockResultSet.getInt("ID_EMPRESA") >> 1
        mockResultSet.getInt("ID_USUARIO") >> 2
        mockResultSet.getInt("ID_LOCALIZACAO") >> 3
        mockResultSet.getString("NOME_EMPRESA") >> "Empresa Teste"
        mockResultSet.getString("CNPJ") >> "12345678901234"

        when: "o método listarEmpresaPorId é chamado"
        def resultado = empresaRepository.listarEmpresaPorId(empresaId)

        then: "deve retornar a empresa correta"
        resultado != null
        resultado.empresaId == 1
        resultado.usuarioId == 2
        resultado.localizacaoId == 3
        resultado.empresaNome == "Empresa Teste"
        resultado.cnpj == "12345678901234"
    }

    def "deve retornar null quando empresa não existe"() {
        given: "um ID de empresa inexistente"
        def empresaId = 999

        and: "configuração dos mocks para busca sem resultado"
        mockConnection.prepareStatement(_) >> mockPreparedStatement
        mockPreparedStatement.executeQuery() >> mockResultSet
        mockResultSet.next() >> false

        when: "o método listarEmpresaPorId é chamado"
        def resultado = empresaRepository.listarEmpresaPorId(empresaId)

        then: "deve retornar null"
        resultado == null
    }

    def "deve listar todas as empresas com sucesso"() {
        given: "configuração dos mocks para listar empresas"
        mockConnection.prepareStatement(_) >> mockPreparedStatement
        mockPreparedStatement.executeQuery() >> mockResultSet
        mockResultSet.next() >>> [true, true, false]

        and: "dados das empresas no resultset"
        mockResultSet.getInt("ID_EMPRESA") >>> [1, 2]
        mockResultSet.getInt("ID_USUARIO") >>> [10, 20]
        mockResultSet.getInt("ID_LOCALIZACAO") >>> [100, 200]
        mockResultSet.getString("NOME_EMPRESA") >>> ["Empresa A", "Empresa B"]
        mockResultSet.getString("CNPJ") >>> ["11111111111111", "22222222222222"]

        when: "o método listarEmpresas é chamado"
        def resultado = empresaRepository.listarEmpresas()

        then: "deve retornar a lista com duas empresas"
        resultado.size() == 2
        resultado[0].empresaId == 1
        resultado[0].empresaNome == "Empresa A"
        resultado[1].empresaId == 2
        resultado[1].empresaNome == "Empresa B"
    }

    def "deve editar empresa com sucesso"() {
        given: "uma empresa para editar"
        def empresa = new Empresa(
                empresaId: 1,
                empresaNome: "Empresa Atualizada",
                cnpj: "98765432109876"
        )

        and: "configuração dos mocks para atualização"
        mockConnection.prepareStatement(_) >> mockPreparedStatement
        mockPreparedStatement.executeUpdate() >> 1

        when: "o método editarEmpresa é chamado"
        def resultado = empresaRepository.editarEmpresa(empresa)

        then: "deve retornar true indicando sucesso"
        resultado == true

        and: "as operações de banco de dados foram chamadas corretamente"
        1 * mockConnection.setAutoCommit(false)
        1 * mockConnection.commit()
        1 * mockConnection.setAutoCommit(true)
        1 * mockConnection.close()
    }

    def "deve excluir empresa com sucesso"() {
        given: "um ID de empresa para excluir"
        def empresaId = 1

        and: "configuração dos mocks para exclusão"
        mockConnection.prepareStatement(_) >>> [
                mockPreparedStatement,
                mockPreparedStatement,
                mockPreparedStatement,
                mockPreparedStatement
        ]
        mockPreparedStatement.executeQuery() >> mockResultSet
        mockResultSet.next() >> true
        mockResultSet.getInt("ID_USUARIO") >> 10
        mockPreparedStatement.executeUpdate() >>> [1, 1, 1]

        when: "o método excluirEmpresa é chamado"
        def resultado = empresaRepository.excluirEmpresa(empresaId)

        then: "deve retornar true indicando sucesso"
        resultado == true

        and: "as operações de banco de dados foram chamadas corretamente"
        1 * mockConnection.setAutoCommit(false)
        1 * mockConnection.commit()
        1 * mockConnection.setAutoCommit(true)
        1 * mockConnection.close()
    }

    def "deve tratar erro ao salvar empresa"() {
        given: "uma empresa para salvar"
        def empresa = new Empresa(
                empresaNome: "Empresa Teste",
                cnpj: "12345678901234"
        )
        def email = "teste@empresa.com"
        def senha = "senha123"
        def descricao = "Descrição da empresa"
        def cep = "12345678"
        def pais = "Brasil"

        and: "configuração para simular um erro"
        mockConnection.prepareStatement(_, Statement.RETURN_GENERATED_KEYS) >> { throw new SQLException("Erro de teste") }

        when: "o método salvarEmpresa é chamado"
        empresaRepository.salvarEmpresa(empresa, email, senha, descricao, cep, pais)

        then: "deve lançar uma exceção"
        def exception = thrown(RuntimeException)
        exception.message.contains("Erro ao criar empresa")

        and: "rollback deve ser chamado"
        1 * mockConnection.rollback()
        1 * mockConnection.setAutoCommit(true)
        1 * mockConnection.close()
    }
}