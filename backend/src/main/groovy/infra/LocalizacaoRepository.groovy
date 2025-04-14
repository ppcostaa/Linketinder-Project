package groovy.infra

import groovy.database.ConnectionFactory
import groovy.database.DatabaseFactory
import groovy.model.Localizacao

import java.sql.*

class LocalizacaoRepository implements ILocalizacaoRepository {
    private final ConnectionFactory connectionFactory

    LocalizacaoRepository() {
        this(DatabaseFactory.createConnectionFactory())
    }

    LocalizacaoRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory
    }

    @Override
    Localizacao salvarLocalizacao(Localizacao localizacao) {
        String sqlLocalizacao = "INSERT INTO LOCALIZACAO (CEP, PAIS) VALUES (?, ?) RETURNING ID_LOCALIZACAO"
        Connection conn = null
        try {
            conn = connectionFactory.createConnection()
            conn.setAutoCommit(false)

            PreparedStatement stmtLocalizacao = conn.prepareStatement(sqlLocalizacao, Statement.RETURN_GENERATED_KEYS)
            stmtLocalizacao.setString(1, "CEP")
            stmtLocalizacao.setString(2, "PAIS")
            stmtLocalizacao.executeUpdate()

            ResultSet rsLocalizacao = stmtLocalizacao.getGeneratedKeys()
            rsLocalizacao.close()
            int affectedRows = stmtLocalizacao.executeUpdate()
            if (affectedRows > 0) {
                if (rsLocalizacao.next()) {
                    localizacao.localizacaoId = rsLocalizacao.getInt(1)
                }
                rsLocalizacao.close()
            }
            conn.commit()
            return localizacao
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback()
                } catch (SQLException ex) {
                    throw new RuntimeException("Erro ao realizar rollback: " + ex.getMessage(), ex)
                }
            }
            throw new RuntimeException("Erro ao salvar localizacao: " + e.getMessage(), e)
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
    List<Localizacao> listarLocalizacoes() {
        final String sql = "SELECT * FROM LOCALIZACAO"
        List<Localizacao> localizacoes = []

        try (Connection conn = connectionFactory.createConnection()) {

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()

            while (rs.next()) {
                Localizacao localizacao = new Localizacao()
                localizacao.localizacaoId = rs.getInt("ID_LOCALIZACAO")
                localizacao.pais = rs.getString("PAIS")
                localizacao.cep = rs.getString("CEP")

                localizacoes.add(localizacao)
            }

            return localizacoes
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar localizacoes: " + e.getMessage(), e)
        }
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