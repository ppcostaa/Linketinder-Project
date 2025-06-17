package DAO

import database.ConnectionFactory
import model.Usuario
import spock.lang.Specification
import spock.lang.Subject

import java.sql.*

class UsuarioDAOTeste extends Specification {

    @Subject
    UsuarioDAO repository

    ConnectionFactory connectionFactory = Mock()
    Connection connection = Mock()
    PreparedStatement preparedStatement = Mock()
    ResultSet resultSet = Mock()

    def setup() {
        repository = new UsuarioDAO()
        repository.connectionFactory = connectionFactory
    }

    def "deve verificar se email existe"() {
        given: "um email válido"
        def email = "teste@example.com"

        and: "mocks configurados"
        connectionFactory.createConnection() >> connection
        connection.prepareStatement(_ as String) >> preparedStatement
        preparedStatement.executeQuery() >> resultSet
        resultSet.next() >> true
        resultSet.getInt(1) >> count

        when: "verifica se email existe"
        def resultado = repository.emailExiste(email)

        then: "retorna o resultado esperado"
        resultado == expected

        where:
        count | expected
        1     | true
        0     | false
    }

    def "deve listar usuário por ID com sucesso"() {
        given: "um ID de usuário válido"
        def usuarioId = 1

        and: "mocks configurados"
        connectionFactory.createConnection() >> connection
        connection.prepareStatement(_ as String) >> preparedStatement
        preparedStatement.executeQuery() >> resultSet
        resultSet.next() >> true
        resultSet.getInt("ID_USUARIO") >> 1
        resultSet.getString("EMAIL") >> "teste@example.com"
        resultSet.getString("SENHA") >> "123456"
        resultSet.getString("DESCRICAO") >> "Descrição teste"

        when: "busca usuário por ID"
        def usuario = repository.listarUsuarioPorId(usuarioId)

        then: "retorna o usuário esperado"
        usuario.usuarioId == 1
        usuario.email == "teste@example.com"
        usuario.senha == "123456"
        usuario.descricao == "Descrição teste"
    }

    def "deve retornar null ao buscar usuário por ID inexistente"() {
        given: "um ID de usuário inexistente"
        def usuarioId = 999

        and: "mocks configurados"
        connectionFactory.createConnection() >> connection
        connection.prepareStatement(_ as String) >> preparedStatement
        preparedStatement.executeQuery() >> resultSet
        resultSet.next() >> false

        when: "busca usuário por ID"
        def usuario = repository.listarUsuarioPorId(usuarioId)

        then: "retorna null"
        usuario == null
    }

    def "deve editar usuário com sucesso"() {
        given: "um usuário válido para edição"
        def usuario = new Usuario(usuarioId: 1, email: "novo@example.com", senha: "novaSenha", descricao: "Nova descrição")

        and: "mocks configurados"
        connectionFactory.createConnection() >> connection
        connection.prepareStatement(_ as String) >> preparedStatement
        preparedStatement.executeUpdate() >> 1

        when: "edita o usuário"
        def resultado = repository.editarUsuario(usuario)

        then: "retorna true indicando sucesso"
        resultado
    }

    def "deve retornar false ao tentar editar usuário inexistente"() {
        given: "um usuário com ID inexistente"
        def usuario = new Usuario(usuarioId: 999, email: "inexistente@example.com", senha: "senha", descricao: "Descrição")

        and: "mocks configurados"
        connectionFactory.createConnection() >> connection
        connection.prepareStatement(_ as String) >> preparedStatement
        preparedStatement.executeUpdate() >> 0

        when: "edita o usuário"
        def resultado = repository.editarUsuario(usuario)

        then: "retorna false indicando falha"
        !resultado
    }
}