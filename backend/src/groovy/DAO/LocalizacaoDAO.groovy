package DAO

import database.ConnectionFactory
import database.DatabaseFactory
import interfaces.ILocalizacaoDAO
import model.Localizacao

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class LocalizacaoDAO implements ILocalizacaoDAO {
    private final ConnectionFactory connectionFactory

    LocalizacaoDAO() {
        this(DatabaseFactory.createConnectionFactory())
    }

    LocalizacaoDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory
    }

    @Override
    Localizacao listarLocalizacoesPorId(int localizacaoId) {
        final String sql = "SELECT * FROM LOCALIZACAO WHERE ID_LOCALIZACAO = ?"

        try (Connection conn = connectionFactory.createConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql)
            stmt.setInt(1, localizacaoId)
            ResultSet rs = stmt.executeQuery()

            if (rs.next()) {
                Localizacao localizacao = new Localizacao()
                localizacao.localizacaoId = rs.getInt("ID_LOCALIZACAO")
                localizacao.pais = rs.getString("PAIS")
                localizacao.cep = rs.getString("CEP")
                return localizacao
            }

            return null
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar localizacao: " + e.getMessage(), e)
        }
    }

    @Override
    boolean editarLocalizacao(Localizacao localizacao, int localizacaoId) {
        final String sql = "UPDATE LOCALIZACAO SET CEP = ?, PAIS = ? WHERE ID_LOCALIZACAO = ?"
        Connection conn = null

        try {
            conn = connectionFactory.createConnection()
            conn.setAutoCommit(false)
            PreparedStatement stmt = conn.prepareStatement(sql)
            stmt.setString(1, localizacao.cep)  // Use actual value from parameter
            stmt.setString(2, localizacao.pais) // Use actual value from parameter
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
            throw new RuntimeException("Erro ao editar localizacao: " + e.getMessage(), e)
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