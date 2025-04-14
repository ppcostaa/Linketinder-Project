package groovy.infra

import groovy.database.ConnectionFactory
import groovy.database.DatabaseFactory
import groovy.model.Competencia
import groovy.model.Vaga

import java.sql.*

class VagaRepository implements IVagaRepository {

    ConnectionFactory connectionFactory = DatabaseFactory.createConnectionFactory()
    CompetenciaRepository competenciaRepository = new CompetenciaRepository()

    @Override
    Vaga salvarVaga(Vaga vaga) {
        Connection conn = null

        try {
            conn = connectionFactory.createConnection()
            conn.setAutoCommit(false)

            String sqlVaga = "INSERT INTO VAGAS (ID_EMPRESA, NOME_VAGA, DESCRICAO_VAGA, LOCAL_ESTADO, LOCAL_CIDADE) VALUES (?, ?, ?, ?, ?) RETURNING ID_VAGA"
            PreparedStatement stmtVaga = conn.prepareStatement(sqlVaga, Statement.RETURN_GENERATED_KEYS)

            stmtVaga.setInt(1, vaga.empresaId)
            stmtVaga.setString(2, vaga.titulo)
            stmtVaga.setString(3, vaga.descricao)
            stmtVaga.setString(4, vaga.estado)
            stmtVaga.setString(5, vaga.cidade)

            int affectedRows = stmtVaga.executeUpdate()

            if (affectedRows > 0) {
                ResultSet rs = stmtVaga.getGeneratedKeys()
                if (rs.next()) {
                    vaga.vagaId = rs.getInt(1)
                }
                rs.close()
            }

            vaga.competencias.each { competencia ->
                String sqlCompetencia = "INSERT INTO VAGA_COMPETENCIAS (ID_VAGA, ID_COMPETENCIA) VALUES (?, ?)"
                PreparedStatement stmtCompetencia = conn.prepareStatement(sqlCompetencia)
                stmtCompetencia.setInt(1, vaga.vagaId)
                stmtCompetencia.setInt(2, competencia.competenciaId)
                stmtCompetencia.executeUpdate()
                stmtCompetencia.close()
            }

            conn.commit()

            return vaga
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback()
                } catch (SQLException ex) {
                    throw new RuntimeException("Erro ao fazer rollback: " + ex.getMessage(), ex)
                }
            }
            throw new RuntimeException("Erro ao criar vaga: " + e.getMessage(), e)
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
    Vaga listarVagaPorId(int vagaId) {
        String sql = "SELECT * FROM VAGAS WHERE ID_VAGA = ?"

        try (Connection conn = connectionFactory.createConnection()) {

            PreparedStatement stmt = conn.prepareStatement(sql)

            stmt.setInt(1, vagaId)
            ResultSet rs = stmt.executeQuery()

            if (rs.next()) {
                Vaga vaga = new Vaga()
                vaga.vagaId = rs.getInt("ID_VAGA")
                vaga.empresaId = rs.getInt("ID_EMPRESA")
                vaga.titulo = rs.getString("NOME_VAGA")
                vaga.descricao = rs.getString("DESCRICAO_VAGA")
                vaga.estado = rs.getString("LOCAL_ESTADO")
                vaga.cidade = rs.getString("LOCAL_CIDADE")

                String sqlCompetencias = "SELECT C.ID_COMPETENCIA, C.NOME_COMPETENCIA FROM COMPETENCIAS C JOIN VAGA_COMPETENCIAS VC ON C.ID_COMPETENCIA = VC.ID_COMPETENCIA WHERE VC.ID_VAGA = ?"
                PreparedStatement stmtCompetencias = conn.prepareStatement(sqlCompetencias)
                stmtCompetencias.setInt(1, vaga.vagaId)
                ResultSet rsCompetencias = stmtCompetencias.executeQuery()

                List<Competencia> competencias = []
                while (rsCompetencias.next()) {
                    Competencia competencia = new Competencia(rsCompetencias.getInt("ID_COMPETENCIA"), rsCompetencias.getString("NOME_COMPETENCIA"))
                    competencias.add(competencia)
                }
                vaga.competencias = competencias

                return vaga
            }

            return null
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar vaga: " + e.getMessage(), e)
        }
    }

    @Override
    List<Vaga> listarVagas() {
        String sql = "SELECT * FROM VAGAS"
        List<Vaga> vagas = []

        try (Connection conn = connectionFactory.createConnection()) {

            PreparedStatement stmt = conn.prepareStatement(sql)
            ResultSet rs = stmt.executeQuery()

            while (rs.next()) {
                Vaga vaga = new Vaga()
                vaga.vagaId = rs.getInt("ID_VAGA")
                vaga.empresaId = rs.getInt("ID_EMPRESA")
                vaga.titulo = rs.getString("NOME_VAGA")
                vaga.descricao = rs.getString("DESCRICAO_VAGA")
                vaga.estado = rs.getString("LOCAL_ESTADO")
                vaga.cidade = rs.getString("LOCAL_CIDADE")

                vaga.competencias = competenciaRepository.listarCompetenciasPorVaga(vaga.vagaId)


                vagas.add(vaga)
            }

            return vagas
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar vagas: " + e.getMessage(), e)
        }
    }

    @Override
    boolean editarVaga(Vaga vaga) {
        String sql = "UPDATE VAGAS SET NOME_VAGA = ?, DESCRICAO_VAGA = ?, LOCAL_ESTADO = ?, LOCAL_CIDADE = ? WHERE ID_VAGA = ?"

        try (Connection conn = connectionFactory.createConnection()) {

            PreparedStatement stmt = conn.prepareStatement(sql)

            stmt.setString(1, vaga.titulo)
            stmt.setString(2, vaga.descricao)
            stmt.setString(3, vaga.estado)
            stmt.setString(4, vaga.cidade)
            stmt.setInt(5, vaga.vagaId)

            int affectedRows = stmt.executeUpdate()
            return affectedRows > 0
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao editar vaga: " + e.getMessage(), e)
        }
    }

    @Override
    boolean excluirVaga(int vagaId) {
        Connection conn = null
        try {
            conn = connectionFactory.createConnection()
            conn.setAutoCommit(false)

            String sqlDeleteCompetencias = "DELETE FROM VAGA_COMPETENCIAS WHERE ID_VAGA = ?"
            PreparedStatement stmtDeleteCompetencias = conn.prepareStatement(sqlDeleteCompetencias)
            stmtDeleteCompetencias.setInt(1, vagaId)
            stmtDeleteCompetencias.executeUpdate()
            stmtDeleteCompetencias.close()

            String sqlDeleteVaga = "DELETE FROM VAGAS WHERE ID_VAGA = ?"
            PreparedStatement stmtDeleteVaga = conn.prepareStatement(sqlDeleteVaga)
            stmtDeleteVaga.setInt(1, vagaId)
            int affectedRows = stmtDeleteVaga.executeUpdate()
            stmtDeleteVaga.close()

            conn.commit()
            return affectedRows > 0
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback()
                } catch (SQLException ex) {
                    throw new RuntimeException("Erro ao fazer rollback: " + ex.getMessage(), ex)
                }
            }
            throw new RuntimeException("Erro ao excluir vaga: " + e.getMessage(), e)
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
}