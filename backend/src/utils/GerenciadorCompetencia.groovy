package utils
    import model.Competencia
    import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
    import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import org.postgresql.core.ConnectionFactory

class GerenciadorCompetencia {



        Competencia create(Competencia competencia) {
            String sql = "INSERT INTO COMPETENCIAS (NOME_COMPETENCIA) VALUES (?) RETURNING ID_COMPETENCIA"

            try (Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/linketinder",
                    "postgres",
                    "senha123"
            )
                 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, competencia.nomeCompetencia)

                int affectedRows = stmt.executeUpdate()

                if (affectedRows > 0) {
                    ResultSet rs = stmt.getGeneratedKeys()
                    if (rs.next()) {
                        competencia.idCompetencia = rs.getInt(1)
                    }
                    rs.close()
                }

                return competencia
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao criar competência: " + e.getMessage(), e)
            }
        }

        Competencia findByName(String nome) {
            String sql = "SELECT * FROM COMPETENCIAS WHERE NOME_COMPETENCIA = ?"

            try (Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/linketinder",
                    "postgres",
                    "senha123")
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, nome)
                ResultSet rs = stmt.executeQuery()

                if (rs.next()) {
                    Competencia competencia = new Competencia()
                    competencia.idCompetencia = rs.getInt("ID_COMPETENCIA")
                    competencia.nomeCompetencia = rs.getString("NOME_COMPETENCIA")
                    return competencia
                }

                return null
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao buscar competência: " + e.getMessage(), e)
            }
        }

        List<Competencia> findAll() {
            String sql = "SELECT * FROM COMPETENCIAS"
            List<Competencia> competencias = []

            try (Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/linketinder",
                    "postgres",
                    "senha123")
                 PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Competencia competencia = new Competencia()
                    competencia.idCompetencia = rs.getInt("ID_COMPETENCIA")
                    competencia.nomeCompetencia = rs.getString("NOME_COMPETENCIA")
                    competencias.add(competencia)
                }

                return competencias
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao listar competências: " + e.getMessage(), e)
            }
        }

        // Implementar métodos update, delete
    }

