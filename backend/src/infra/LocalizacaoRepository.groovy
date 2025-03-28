package infra

import database.ConnectionFactory
import model.Localizacao

import java.sql.*

class LocalizacaoRepository implements ILocalizacaoRepository {
    ConnectionFactory connectionFactory = new ConnectionFactory(
            'jdbc:postgresql://localhost:5432/linketinder', 'postgres', 'senha123'
    )

    @Override
    Localizacao salvarLocalizacao(Localizacao localizacao) {
        String sql = "INSERT INTO LOCALIZACAO (CEP, PAIS) VALUES (?, ?) RETURNING ID_LOCALIZACAO"

        try (Connection conn = connectionFactory.createConnection()) {

            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

            stmt.setString(1, localizacao.cep)
            stmt.setString(2, localizacao.pais)

            int affectedRows = stmt.executeUpdate()

            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys()
                if (rs.next()) {
                    localizacao.localizacaoId = rs.getInt(1)
                }
                rs.close()
            }

            return localizacao
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar localização: " + e.getMessage(), e)
        }

    }

    @Override
    List<Localizacao> listarLocalizacoes() {
        String sql = "SELECT * FROM LOCALIZACAO"
        List<Localizacao> localizacoes = []

        try (Connection conn = connectionFactory.createConnection()) {

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()

            while (rs.next()) {
                Localizacao localizacao = new Localizacao()
                localizacao.localizacaoId = rs.getInt("ID_LOCALIZACAO")
                localizacao.cep = rs.getString("CEP")
                localizacao.pais = rs.getString("PAIS")
                localizacoes.add(localizacao)
            }

            return localizacoes
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar localizações: " + e.getMessage(), e)
        }
    }

    @Override
    Localizacao listarLocalizacaoPorId(int localizacaoId) {
        String sql = "SELECT * FROM LOCALIZACAO WHERE ID_LOCALIZACAO = ?"

        try (Connection conn = connectionFactory.createConnection()) {

            PreparedStatement stmt = conn.prepareStatement(sql)

            stmt.setObject(1, localizacaoId ?: null)
            ResultSet rs = stmt.executeQuery()

            if (rs.next()) {
                Localizacao localizacao = new Localizacao()
                localizacao.localizacaoId = rs.getInt("ID_LOCALIZACAO")
                localizacao.cep = rs.getString("CEP")
                localizacao.pais = rs.getString("PAIS")
                return localizacao
            }

            return null
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar localização: " + e.getMessage(), e)
        }
    }

    @Override
    boolean editarLocalizacao(Localizacao localizacao, int localizacaoId) {
        Connection conn = null
        try {
            conn = connectionFactory.createConnection()
            conn.setAutoCommit(false)

            String sql = "UPDATE LOCALIZACAO SET CEP = ?, PAIS = ? WHERE ID_LOCALIZACAO = ?"
            PreparedStatement stmt = conn.prepareStatement(sql)
            stmt.setString(1, localizacao.cep)
            stmt.setString(2, localizacao.pais)
            stmt.setInt(3, localizacaoId)

            int affectedRows = stmt.executeUpdate()

            conn.commit()
            return affectedRows > 0
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback()
                } catch (SQLException ex) {
                    throw new RuntimeException("Erro ao realizar rollback: " + ex.getMessage(), ex)
                }
            }
            throw new RuntimeException("Erro ao atualizar localização: " + e.getMessage(), e)
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
