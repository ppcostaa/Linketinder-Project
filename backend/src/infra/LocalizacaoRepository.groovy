package infra

import database.ConnectionFactory
import database.DatabaseFactory
import model.Localizacao

import java.sql.*

class LocalizacaoRepository implements ILocalizacaoRepository {
    private final ConnectionFactory connectionFactory

    LocalizacaoRepository() {
        this.connectionFactory = DatabaseFactory.createConnectionFactory()
    }

    LocalizacaoRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory
    }

    @Override
    Localizacao salvarLocalizacao(Localizacao localizacao) {
        final String sql = "INSERT INTO LOCALIZACAO (CEP, PAIS) VALUES (?, ?) RETURNING ID_LOCALIZACAO"

        try (Connection conexao = obterConexao()) {
            PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            preencherParametrosLocalizacao(comando, localizacao)

            int linhasAfetadas = comando.executeUpdate()
            if (linhasAfetadas > 0) {
                atribuirIdGerado(comando, localizacao)
            }

            return localizacao
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao salvar localização: " + e.getMessage(), e)
        }
    }

    @Override
    List<Localizacao> buscarTodasLocalizacoes() {
        final String sql = "SELECT * FROM LOCALIZACAO"
        List<Localizacao> localizacoes = []

        try (Connection conexao = obterConexao()) {
            PreparedStatement consulta = conexao.prepareStatement(sql)
            ResultSet resultado = consulta.executeQuery()

            while (resultado.next()) {
                localizacoes.add(criarLocalizacaoAPartirDoResultado(resultado))
            }

            return localizacoes
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao listar localizações: " + e.getMessage(), e)
        }
    }

    @Override
    Localizacao buscarLocalizacaoPorId(int localizacaoId) {
        final String sql = "SELECT * FROM LOCALIZACAO WHERE ID_LOCALIZACAO = ?"

        try (Connection conexao = obterConexao()) {
            PreparedStatement consulta = conexao.prepareStatement(sql)
            consulta.setInt(1, localizacaoId)

            ResultSet resultado = consulta.executeQuery()
            return resultado.next() ? criarLocalizacaoAPartirDoResultado(resultado) : null
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao buscar localização por ID: " + e.getMessage(), e)
        }
    }

    @Override
    boolean atualizarLocalizacao(Localizacao localizacao, int localizacaoId) {
        final String sql = "UPDATE LOCALIZACAO SET CEP = ?, PAIS = ? WHERE ID_LOCALIZACAO = ?"
        Connection conexao = null

        try {
            conexao = obterConexao()
            conexao.setAutoCommit(false)

            PreparedStatement comando = conexao.prepareStatement(sql)
            preencherParametrosLocalizacao(comando, localizacao)
            comando.setInt(3, localizacaoId)

            int linhasAfetadas = comando.executeUpdate()
            conexao.commit()

            return linhasAfetadas > 0
        } catch (SQLException e) {
            realizarRollback(conexao)
            throw new RuntimeException("Falha ao atualizar localização: " + e.getMessage(), e)
        } finally {
            fecharConexao(conexao)
        }
    }

    private Connection obterConexao() throws SQLException {
        return connectionFactory.createConnection()
    }

    private void preencherParametrosLocalizacao(PreparedStatement comando, Localizacao localizacao) throws SQLException {
        comando.setString(1, localizacao.cep)
        comando.setString(2, localizacao.pais)
    }

    private void atribuirIdGerado(PreparedStatement comando, Localizacao localizacao) throws SQLException {
        ResultSet chavesGeradas = comando.getGeneratedKeys()
        if (chavesGeradas.next()) {
            localizacao.localizacaoId = chavesGeradas.getInt(1)
        }
        chavesGeradas.close()
    }

    private Localizacao criarLocalizacaoAPartirDoResultado(ResultSet resultado) throws SQLException {
        return new Localizacao(
                resultado.getInt("ID_LOCALIZACAO"),
                resultado.getString("CEP"),
                resultado.getString("PAIS")
        )
    }

    private void realizarRollback(Connection conexao) {
        if (conexao != null) {
            try {
                conexao.rollback()
            } catch (SQLException e) {
                throw new RuntimeException("Falha ao realizar rollback: " + e.getMessage(), e)
            }
        }
    }

    private void fecharConexao(Connection conexao) {
        if (conexao != null) {
            try {
                conexao.setAutoCommit(true)
                conexao.close()
            } catch (SQLException e) {
                throw new RuntimeException("Falha ao fechar conexão: " + e.getMessage(), e)
            }
        }
    }
}