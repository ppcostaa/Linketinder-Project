package infra

import database.ConnectionFactory
import database.DatabaseFactory
import model.Usuario

import java.sql.*

class UsuarioRepository implements IUsuarioRepository {
    private final ConnectionFactory connectionFactory

    UsuarioRepository() {
        this.connectionFactory = DatabaseFactory.createConnectionFactory()
    }

    UsuarioRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory
    }

    @Override
    boolean emailExiste(String email) {
        final String sql = "SELECT COUNT(*) FROM USUARIOS WHERE EMAIL = ?"

        try (Connection conexao = obterConexao()) {
            PreparedStatement consulta = conexao.prepareStatement(sql)
            consulta.setString(1, email)

            ResultSet resultado = consulta.executeQuery()
            resultado.next()
            return resultado.getInt(1) > 0
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao verificar existência do email: " + e.getMessage(), e)
        }
    }

    @Override
    Usuario salvarUsuario(Usuario usuario) {
        final String sql = "INSERT INTO USUARIOS (EMAIL, SENHA, DESCRICAO) VALUES (?, ?, ?) RETURNING ID_USUARIO"

        try (Connection conexao = obterConexao()) {
            PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            preencherParametrosUsuario(comando, usuario)

            int linhasAfetadas = comando.executeUpdate()
            if (linhasAfetadas > 0) {
                atribuirIdGerado(comando, usuario)
            }

            return usuario
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao salvar usuário: " + e.getMessage(), e)
        }
    }

    @Override
    Usuario buscarUsuarioPorId(int usuarioId) {
        final String sql = "SELECT * FROM USUARIOS WHERE ID_USUARIO = ?"

        try (Connection conexao = obterConexao()) {
            PreparedStatement consulta = conexao.prepareStatement(sql)
            consulta.setInt(1, usuarioId)

            ResultSet resultado = consulta.executeQuery()
            return resultado.next() ? criarUsuarioAPartirDoResultado(resultado) : null
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao buscar usuário por ID: " + e.getMessage(), e)
        }
    }

    @Override
    List<Usuario> buscarTodosUsuarios() {
        final String sql = "SELECT * FROM USUARIOS"
        List<Usuario> usuarios = []

        try (Connection conexao = obterConexao()) {
            PreparedStatement consulta = conexao.prepareStatement(sql)
            ResultSet resultado = consulta.executeQuery()

            while (resultado.next()) {
                usuarios.add(criarUsuarioAPartirDoResultado(resultado))
            }

            return usuarios
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao listar todos os usuários: " + e.getMessage(), e)
        }
    }

    @Override
    boolean atualizarUsuario(Usuario usuario) {
        final String sql = "UPDATE USUARIOS SET EMAIL = ?, SENHA = ?, DESCRICAO = ? WHERE ID_USUARIO = ?"

        try (Connection conexao = obterConexao()) {
            PreparedStatement comando = conexao.prepareStatement(sql)
            preencherParametrosUsuario(comando, usuario)
            comando.setInt(4, usuario.usuarioId)

            return comando.executeUpdate() > 0
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao atualizar usuário: " + e.getMessage(), e)
        }
    }

    @Override
    boolean removerUsuario(int usuarioId) {
        final String sql = "DELETE FROM USUARIOS WHERE ID_USUARIO = ?"

        try (Connection conexao = obterConexao()) {
            PreparedStatement comando = conexao.prepareStatement(sql)
            comando.setInt(1, usuarioId)

            return comando.executeUpdate() > 0
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao remover usuário: " + e.getMessage(), e)
        }
    }
    private Connection obterConexao() throws SQLException {
        return connectionFactory.createConnection()
    }

    private void preencherParametrosUsuario(PreparedStatement comando, Usuario usuario) throws SQLException {
        comando.setString(1, usuario.email)
        comando.setString(2, usuario.senha)
        comando.setString(3, usuario.descricao)
    }

    private void atribuirIdGerado(PreparedStatement comando, Usuario usuario) throws SQLException {
        ResultSet chavesGeradas = comando.getGeneratedKeys()
        if (chavesGeradas.next()) {
            usuario.usuarioId = chavesGeradas.getInt(1)
        }
        chavesGeradas.close()
    }

    private Usuario criarUsuarioAPartirDoResultado(ResultSet resultado) throws SQLException {
        return new Usuario(
                resultado.getInt("ID_USUARIO"),
                resultado.getString("EMAIL"),
                resultado.getString("SENHA"),
                resultado.getString("DESCRICAO")
        )
    }
}