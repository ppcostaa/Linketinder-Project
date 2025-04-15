package repository

import database.ConnectionFactory
import database.DatabaseFactory
import interfaces.IUsuarioRepository
import model.Usuario

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class UsuarioRepository implements IUsuarioRepository {
    ConnectionFactory connectionFactory = DatabaseFactory.createConnectionFactory()

    @Override
    boolean emailExiste(String email) {
        try (Connection conn = connectionFactory.createConnection()) {

            PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM USUARIOS WHERE EMAIL = ?")

            stmt.setString(1, email)
            ResultSet rs = stmt.executeQuery()
            rs.next()
            int count = rs.getInt(1)
            return count > 0
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar email: " + e.getMessage(), e)
        }

    }

    @Override
    Usuario listarUsuarioPorId(int usuarioId) {
        String sql = "SELECT * FROM USUARIOS WHERE ID_USUARIO = ?"

        try (Connection conn = connectionFactory.createConnection()) {

            PreparedStatement stmt = conn.prepareStatement(sql)

            stmt.setObject(1, usuarioId ?: null)
            ResultSet rs = stmt.executeQuery()

            if (rs.next()) {
                Usuario usuario = new Usuario()
                usuario.usuarioId = rs.getInt("ID_USUARIO")
                usuario.email = rs.getString("EMAIL")
                usuario.senha = rs.getString("SENHA")
                usuario.descricao = rs.getString("DESCRICAO")
                return usuario
            }

            return null
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário: " + e.getMessage(), e)
        }
    }

    @Override
    boolean editarUsuario(Usuario usuario) {
        String sql = "UPDATE USUARIOS SET EMAIL = ?, SENHA = ?, DESCRICAO = ? WHERE ID_USUARIO = ?"

        try (Connection conn = connectionFactory.createConnection()) {

            PreparedStatement stmt = conn.prepareStatement(sql)

            stmt.setString(1, usuario.email)
            stmt.setString(2, usuario.senha)
            stmt.setString(3, usuario.descricao)
            stmt.setObject(4, usuario.usuarioId ?: null)

            int affectedRows = stmt.executeUpdate()
            return affectedRows > 0
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário: " + e.getMessage(), e)
        }
    }
}
