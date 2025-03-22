package infra

import database.ConnectionFactory
import model.Vaga

import java.sql.*

class VagaRepository implements IVagaRepository {

    ConnectionFactory connectionFactory = new ConnectionFactory(
            'jdbc:postgresql://localhost:5432/linketinder', 'postgres', 'senha123'
    )

    @Override
    Vaga salvarVaga(Vaga vaga) {
        String sql = "INSERT INTO VAGAS (ID_EMPRESA, NOME_VAGA, DESCRICAO_VAGA, LOCAL_ESTADO, LOCAL_CIDADE) VALUES (?, ?, ?, ?, ?) RETURNING ID_VAGA"

        try (Connection conn = connectionFactory.createConnection()) {

            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

            stmt.setInt(1, vaga.empresaId)
            stmt.setString(2, vaga.vagaNome)
            stmt.setString(3, vaga.descricaoVaga)
            stmt.setString(4, vaga.estado)
            stmt.setString(5, vaga.cidade)

            int affectedRows = stmt.executeUpdate()

            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys()
                if (rs.next()) {
                    vaga.vagaId = rs.getInt(1)
                }
                rs.close()
            }

            return vaga
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar vaga: " + e.getMessage(), e)
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
                vaga.vagaNome = rs.getString("NOME_VAGA")
                vaga.descricaoVaga = rs.getString("DESCRICAO_VAGA")
                vaga.estado = rs.getString("LOCAL_ESTADO")
                vaga.cidade = rs.getString("LOCAL_CIDADE")
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

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()

            while (rs.next()) {
                Vaga vaga = new Vaga()
                vaga.vagaId = rs.getInt("ID_VAGA")
                vaga.empresaId = rs.getInt("ID_EMPRESA")
                vaga.vagaNome = rs.getString("NOME_VAGA")
                vaga.descricaoVaga = rs.getString("DESCRICAO_VAGA")
                vaga.estado = rs.getString("LOCAL_ESTADO")
                vaga.cidade = rs.getString("LOCAL_CIDADE")
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

            stmt.setString(1, vaga.vagaNome)
            stmt.setString(2, vaga.descricaoVaga)
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
        String sql = "DELETE FROM VAGAS WHERE ID_VAGA = ?"

        try (Connection conn = connectionFactory.createConnection()) {

            PreparedStatement stmt = conn.prepareStatement(sql)

            stmt.setInt(1, vagaId)

            int affectedRows = stmt.executeUpdate()
            return affectedRows > 0
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir vaga: " + e.getMessage(), e)
        }
    }

}
