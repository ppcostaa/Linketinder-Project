package interfaces

import database.ConnectionFactory
import model.Usuario
import repository.UsuarioRepository
import spock.lang.Specification
import spock.lang.Subject

import java.sql.*

class UsuarioRepositoryTeste extends Specification {

    @Subject
    UsuarioRepository repository

    ConnectionFactory connectionFactory = Mock()
    Connection connection = Mock()
    PreparedStatement preparedStatement = Mock()
    ResultSet resultSet = Mock()

    def setup() {
        repository = new UsuarioRepository()
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

    def "deve salvar um usuário com sucesso"() {
        given: "um usuário válido"
        def usuario = new Usuario(email: "teste@example.com", senha: "123456", descricao: "Descrição teste")

        and: "mocks configurados completamente"
        connectionFactory.createConnection() >> connection
        connection.setAutoCommit(false) >> null

        connection.prepareStatement(_, Statement.RETURN_GENERATED_KEYS) >> preparedStatement
        preparedStatement.setString(1, usuario.email) >> null
        preparedStatement.setString(2, usuario.senha) >> null
        preparedStatement.setString(3, usuario.descricao) >> null
        preparedStatement.executeUpdate() >> 1

        preparedStatement.getGeneratedKeys() >> resultSet
        resultSet.next() >> true
        resultSet.getInt(1) >> 123

        when: "tenta salvar o usuário"
        def resultado = repository.salvarUsuario(usuario)

        then: "o usuário é salvo com ID gerado"
        resultado.usuarioId == 123
        resultado.email == "teste@example.com"
        resultado.senha == "123456"
        resultado.descricao == "Descrição teste"

        and: "verificações de interações"
        1 * connection.commit()
        1 * connection.close()
    }

    def "deve lançar exceção e fazer rollback ao falhar ao salvar usuário"() {
        given: "um usuário válido"
        def usuario = new Usuario(email: "teste@example.com", senha: "123456", descricao: "Descrição teste")

        and: "mocks configurados para simular falha"
        connectionFactory.createConnection() >> connection
        connection.setAutoCommit(false) >> null

        connection.prepareStatement(_, Statement.RETURN_GENERATED_KEYS) >> preparedStatement
        preparedStatement.setString(1, usuario.email) >> { throw new SQLException("Erro de banco") }

        when: "tenta salvar o usuário"
        repository.salvarUsuario(usuario)

        then: "uma exceção é lançada"
        thrown(RuntimeException)

        and: "verificações de rollback e fechamento"
        1 * connection.rollback()
        1 * connection.close()
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

    def "deve listar todos os usuários com sucesso"() {
        given: "mocks configurados"
        connectionFactory.createConnection() >> connection
        connection.prepareStatement(_ as String) >> preparedStatement
        preparedStatement.executeQuery() >> resultSet
        resultSet.next() >>> [true, true, false]
        resultSet.getInt("ID_USUARIO") >>> [1, 2]
        resultSet.getString("EMAIL") >>> ["teste1@example.com", "teste2@example.com"]
        resultSet.getString("SENHA") >>> ["senha1", "senha2"]
        resultSet.getString("DESCRICAO") >>> ["Descrição 1", "Descrição 2"]

        when: "lista todos os usuários"
        def usuarios = repository.listarUsuarios()

        then: "retorna lista com todos usuários"
        usuarios.size() == 2
        usuarios[0].usuarioId == 1
        usuarios[0].email == "teste1@example.com"
        usuarios[0].senha == "senha1"
        usuarios[0].descricao == "Descrição 1"
        usuarios[1].usuarioId == 2
        usuarios[1].email == "teste2@example.com"
        usuarios[1].senha == "senha2"
        usuarios[1].descricao == "Descrição 2"
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

    def "deve excluir usuário com sucesso"() {
        given: "um ID de usuário existente"
        def usuarioId = 1

        and: "mocks configurados"
        connectionFactory.createConnection() >> connection
        connection.prepareStatement(_ as String) >> preparedStatement
        preparedStatement.executeUpdate() >> 1

        when: "exclui o usuário"
        def resultado = repository.excluirUsuario(usuarioId)

        then: "retorna true indicando sucesso"
        resultado
    }

    def "deve retornar false ao tentar excluir usuário inexistente"() {
        given: "um ID de usuário inexistente"
        def usuarioId = 999

        and: "mocks configurados"
        connectionFactory.createConnection() >> connection
        connection.prepareStatement(_ as String) >> preparedStatement
        preparedStatement.executeUpdate() >> 0

        when: "exclui o usuário"
        def resultado = repository.excluirUsuario(usuarioId)

        then: "retorna false indicando falha"
        !resultado
    }
}