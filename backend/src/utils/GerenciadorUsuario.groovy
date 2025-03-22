package utils
    import model.Usuario
    import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
    import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement


class GerenciadorUsuario {

        boolean emailExists(String email) {
            try (Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/linketinder",
                    "postgres",
                    "senha123");
                 PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM USUARIOS WHERE EMAIL = ?")) {

                stmt.setString(1, email)
                ResultSet rs = stmt.executeQuery()
                rs.next()
                int count = rs.getInt(1)
                return count > 0
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao verificar email: " + e.getMessage(), e)
            }
        }

        Usuario create(Usuario usuario) {
            String sql = "INSERT INTO USUARIOS (EMAIL, SENHA, DESCRICAO) VALUES (?, ?, ?) RETURNING ID_USUARIO"

            try (Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/linketinder",
                    "postgres",
                    "senha123")
                 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, usuario.email)
                stmt.setString(2, usuario.senha)
                stmt.setString(3, usuario.descricao)

                int affectedRows = stmt.executeUpdate()

                if (affectedRows > 0) {
                    ResultSet rs = stmt.getGeneratedKeys()
                    if (rs.next()) {
                        usuario.idUsuario = rs.getInt(1)
                    }
                    rs.close()
                }

                return usuario
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao criar usuário: " + e.getMessage(), e)
            }
        }


        Usuario findById(Integer id) {
            String sql = "SELECT * FROM USUARIOS WHERE ID_USUARIO = ?"

            try (Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/linketinder",
                    "postgres",
                    "senha123")
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setObject(1, id ?: null)
                ResultSet rs = stmt.executeQuery()

                if (rs.next()) {
                    Usuario usuario = new Usuario()
                    usuario.idUsuario = rs.getInt("ID_USUARIO")
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

        List<Usuario> findAll() {
            String sql = "SELECT * FROM USUARIOS"
            List<Usuario> usuarios = []

            try (Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/linketinder",
                    "postgres",
                    "senha123")
                 PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Usuario usuario = new Usuario()
                    usuario.idUsuario = rs.getInt("ID_USUARIO")
                    usuario.email = rs.getString("EMAIL")
                    usuario.senha = rs.getString("SENHA")
                    usuario.descricao = rs.getString("DESCRICAO")
                    usuarios.add(usuario)
                }

                return usuarios
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao listar usuários: " + e.getMessage(), e)
            }
        }

        boolean update(Usuario usuario) {
            String sql = "UPDATE USUARIOS SET EMAIL = ?, SENHA = ?, DESCRICAO = ? WHERE ID_USUARIO = ?"

            try (Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/linketinder",
                    "postgres",
                    "senha123")
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, usuario.email)
                stmt.setString(2, usuario.senha)
                stmt.setString(3, usuario.descricao)
                stmt.setObject(4, usuario.idUsuario ?: null)

                int affectedRows = stmt.executeUpdate()
                return affectedRows > 0
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao atualizar usuário: " + e.getMessage(), e)
            }
        }

        boolean delete(Integer id) {
            String sql = "DELETE FROM USUARIOS WHERE ID_USUARIO = ?"

            try (Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/linketinder",
                    "postgres",
                    "senha123")
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setObject(1, id ?: null)

                int affectedRows = stmt.executeUpdate()
                return affectedRows > 0
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao excluir usuário: " + e.getMessage(), e)
            }}
}
