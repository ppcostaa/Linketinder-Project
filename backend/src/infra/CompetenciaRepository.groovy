package infra

import database.ConnectionFactory
import database.DatabaseFactory
import model.Competencia

import java.sql.*

class CompetenciaRepository implements ICompetenciaRepository {
    ConnectionFactory connectionFactory = DatabaseFactory.createConnectionFactory()
    @Override
    Competencia salvarCompetencia(Competencia competencia) {
        Connection conn = null

        try {
            conn = connectionFactory.createConnection()
            conn.setAutoCommit(false)

            String sqlCompetencia = "INSERT INTO COMPETENCIAS (ID_COMPETENCIA, NOME_COMPETENCIA) VALUES (?, ?) RETURNING ID_COMPETENCIA"
            PreparedStatement stmtCompetencia = conn.prepareStatement(sqlCompetencia, Statement.RETURN_GENERATED_KEYS)

            stmtCompetencia.setInt(1, competencia.competenciaId)
            stmtCompetencia.setString(2, competencia.competenciaNome)

            int affectedRows = stmtCompetencia.executeUpdate()

            if (affectedRows > 0) {
                ResultSet rs = stmtCompetencia.getGeneratedKeys()
                if (rs.next()) {
                    competencia.competenciaId = rs.getInt(1)
                }
                rs.close()
            }

            conn.commit()

            return competencia
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback()
                } catch (SQLException ex) {
                    throw new RuntimeException("Erro ao fazer rollback: " + ex.getMessage(), ex)
                }
            }
            throw new RuntimeException("Erro ao criar competência: " + e.getMessage(), e)
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true)
                    conn.close()
                } catch (SQLException e) {
                    throw new RuntimeException("Erro ao fechar conexão: " + e.getMessage(), e)
                }
            }
        }
    }

    List<Competencia> listarCompetencias(int candidatoId) {
        String sql = """
        SELECT c.* 
        FROM COMPETENCIAS c
        JOIN CANDIDATO_COMPETENCIAS cc ON c.ID_COMPETENCIA = cc.ID_COMPETENCIA
        WHERE cc.ID_CANDIDATO = ?
    """

        List<Competencia> competencias = []

        try (Connection conn = connectionFactory.createConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql)
            stmt.setInt(1, candidatoId)
            ResultSet rs = stmt.executeQuery()

            while (rs.next()) {
                Competencia competencia = new Competencia()
                competencia.competenciaId = rs.getInt("ID_COMPETENCIA")
                competencia.competenciaNome = rs.getString("NOME_COMPETENCIA")
                competencias.add(competencia)
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar competências: " + e.getMessage(), e)
        }

        return competencias
    }
    List<Competencia> listarCompetencias() {
        String sql = "SELECT * FROM COMPETENCIAS"

        List<Competencia> competencias = []

        try (Connection conn = connectionFactory.createConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql)
            ResultSet rs = stmt.executeQuery()

            while (rs.next()) {
                Competencia competencia = new Competencia()
                competencia.competenciaId = rs.getInt("ID_COMPETENCIA")
                competencia.competenciaNome = rs.getString("NOME_COMPETENCIA")
                competencias.add(competencia)
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar competências: " + e.getMessage(), e)
        }

        return competencias
    }

    Competencia competenciaPorNome(String nome) {
        String sql = "SELECT * FROM COMPETENCIAS WHERE NOME_COMPETENCIA = ?"

        try (Connection conn = connectionFactory.createConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql)
            stmt.setString(1, nome)
            ResultSet rs = stmt.executeQuery()

            if (rs.next()) {
                Competencia competencia = new Competencia()
                competencia.competenciaId = rs.getInt("ID_COMPETENCIA")
                competencia.competenciaNome = rs.getString("NOME_COMPETENCIA")
                return competencia
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar competência: " + e.getMessage(), e)
        }

        return null
    }

    boolean editarCompetencia(Competencia competencia) {
        Connection conn = null

        try {
            conn = connectionFactory.createConnection()
            conn.setAutoCommit(false)

            String sql = "UPDATE COMPETENCIAS SET NOME_COMPETENCIA = ? WHERE ID_COMPETENCIA = ?"
            PreparedStatement stmt = conn.prepareStatement(sql)
            stmt.setString(1, competencia.competenciaNome)
            stmt.setInt(2, competencia.competenciaId)

            conn.commit()
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback()
                } catch (SQLException ex) {
                    throw new RuntimeException("Erro ao realizar rollback: " + ex.getMessage(), ex)
                }
            }
            throw new RuntimeException("Erro ao atualizar competência: " + e.getMessage(), e)
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true)
                    conn.close()
                } catch (SQLException e) {
                    throw new RuntimeException("Erro ao fechar conexão: " + e.getMessage(), e)
                }
            }
        }
    }

    @Override
    Competencia listarCompetenciasPorId(int competenciaId) {
        String sql = """SELECT * FROM COMPETENCIAS WHERE ID_COMPETENCIA = ?
        """

        try (Connection conn = connectionFactory.createConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql)

            stmt.setInt(1, competenciaId)
            ResultSet rs = stmt.executeQuery()

            if (rs.next()) {
                Competencia competencia = new Competencia()
                competencia.competenciaId = rs.getInt("ID_COMPETENCIA")
                competencia.competenciaNome = rs.getString("NOME_COMPETENCIA")

                return competencia
            }

            return null
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar competência: " + e.getMessage(), e)
        }
    }

    @Override
    boolean excluirCompetencia(int competenciaId) {
        String sql = "DELETE FROM COMPETENCIAS WHERE ID_COMPETENCIA = ?"

        try (Connection conn = connectionFactory.createConnection()) {

            PreparedStatement stmt = conn.prepareStatement(sql)

            stmt.setInt(1, competenciaId)

            int affectedRows = stmt.executeUpdate()
            return affectedRows > 0
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir competência: " + e.getMessage(), e)
        }
    }
}