package infra

import database.ConnectionFactory
import model.Empresa
import model.Localizacao
import model.Usuario

import java.sql.*

class EmpresaRepository implements IEmpresaRepository {
    ConnectionFactory connectionFactory = new ConnectionFactory(
            'jdbc:postgresql://localhost:5432/linketinder', 'postgres', 'senha123'
    )

    @Override
    Empresa salvarEmpresa(Empresa empresa, String email, String senha, String descricao, String cep, String pais) {
        Connection conn = null
        try {
            conn = connectionFactory.createConnection()
            conn.setAutoCommit(false)

            String sqlUsuario = "INSERT INTO USUARIOS (EMAIL, SENHA, DESCRICAO) VALUES (?, ?, ?) RETURNING ID_USUARIO"
            PreparedStatement stmtUsuario = conn.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)
            stmtUsuario.setString(1, email)
            stmtUsuario.setString(2, senha)
            stmtUsuario.setString(3, descricao)
            stmtUsuario.executeUpdate()

            ResultSet rsUsuario = stmtUsuario.getGeneratedKeys()
            int usuarioId = 0
            if (rsUsuario.next()) {
                usuarioId = rsUsuario.getInt(1)
            }
            rsUsuario.close()

            String sqlLocalizacao = "INSERT INTO LOCALIZACAO (CEP, PAIS) VALUES (?, ?) RETURNING ID_LOCALIZACAO"
            PreparedStatement stmtLocalizacao = conn.prepareStatement(sqlLocalizacao, Statement.RETURN_GENERATED_KEYS)
            stmtLocalizacao.setString(1, cep)
            stmtLocalizacao.setString(2, pais)
            stmtLocalizacao.executeUpdate()

            ResultSet rsLocalizacao = stmtLocalizacao.getGeneratedKeys()
            int localizacaoId = 0
            if (rsLocalizacao.next()) {
                localizacaoId = rsLocalizacao.getInt(1)
            }
            rsLocalizacao.close()

            String sql = "INSERT INTO EMPRESAS (ID_USUARIO, ID_LOCALIZACAO, NOME_EMPRESA, CNPJ) VALUES (?, ?, ?, ?) RETURNING ID_EMPRESA"
            GerenciadorLocalizacao
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            stmt.setInt(1, empresa.usuarioId)
            stmt.setInt(2, empresa.localizacaoId)
            stmt.setString(3, empresa.empresaNome)
            stmt.setString(4, empresa.cnpj)

            int affectedRows = stmt.executeUpdate()

            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys()
                if (rs.next()) {
                    empresa.empresaId = rs.getInt(1)
                }
                rs.close()
            }

            conn.commit()
            return empresa
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback()
                } catch (SQLException ex) {
                    throw new RuntimeException("Erro ao realizar rollback: " + ex.getMessage(), ex)
                }
            }
            throw new RuntimeException("Erro ao criar empresa: " + e.getMessage(), e)
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
    Empresa listarEmpresaPorId(int empresaId) {
        String sql = """
            SELECT e.*, u.EMAIL, u.DESCRICAO, l.CEP, l.PAIS 
            FROM EMPRESAS e
            JOIN USUARIOS u ON e.ID_USUARIO = u.ID_USUARIO
            JOIN LOCALIZACAO l ON e.ID_LOCALIZACAO = l.ID_LOCALIZACAO
            WHERE e.ID_EMPRESA = ?
        """

        try (Connection conn = connectionFactory.createConnection()) {

            PreparedStatement stmt = conn.prepareStatement(sql)

            stmt.setInt(1, empresaId)
            ResultSet rs = stmt.executeQuery()

            if (rs.next()) {
                Empresa empresa = new Empresa()
                empresa.empresaId = rs.getInt("ID_EMPRESA")
                empresa.usuarioId = rs.getInt("ID_USUARIO")
                empresa.localizacaoId = rs.getInt("ID_LOCALIZACAO")
                empresa.empresaNome = rs.getString("NOME_EMPRESA")
                empresa.cnpj = rs.getString("CNPJ")

                return empresa
            }

            return null
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar empresa: " + e.getMessage(), e)
        }
    }

    @Override
    List<Empresa> listarEmpresas() {
        String sql = """
            SELECT e.*, u.EMAIL, u.DESCRICAO, l.CEP, l.PAIS 
            FROM EMPRESAS e
            JOIN USUARIOS u ON e.ID_USUARIO = u.ID_USUARIO
            JOIN LOCALIZACAO l ON e.ID_LOCALIZACAO = l.ID_LOCALIZACAO
        """

        List<Empresa> empresas = []

        try (Connection conn = connectionFactory.createConnection()) {

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()

            while (rs.next()) {
                Empresa empresa = new Empresa()
                empresa.empresaId = rs.getInt("ID_EMPRESA")
                empresa.usuarioId = rs.getInt("ID_USUARIO")
                empresa.localizacaoId = rs.getInt("ID_LOCALIZACAO")
                empresa.empresaNome = rs.getString("NOME_EMPRESA")
                empresa.cnpj = rs.getString("CNPJ")

                empresas.add(empresa)
            }

            return empresas
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar empresas: " + e.getMessage(), e)
        }
    }

    @Override
    boolean editarEmpresa(Empresa empresa) {
        String sql = "UPDATE EMPRESAS SET NOME_EMPRESA = ?, CNPJ = ? WHERE ID_EMPRESA = ?"

        try (Connection conn = connectionFactory.createConnection()) {

            PreparedStatement stmt = conn.prepareStatement(sql)

            stmt.setString(1, empresa.empresaNome)
            stmt.setString(2, empresa.cnpj)
            stmt.setInt(3, empresa.empresaId)

            int affectedRows = stmt.executeUpdate()
            return affectedRows > 0
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar empresa: " + e.getMessage(), e)
        }
    }

    @Override
    boolean excluirEmpresa(int empresaId) {
        Connection conn = null;

        try {
            conn = connectionFactory.createConnection();
            conn.setAutoCommit(false);

            String sqlVagas = "DELETE FROM VAGAS WHERE ID_EMPRESA = ?";
            PreparedStatement stmtVagas = conn.prepareStatement(sqlVagas);
            stmtVagas.setInt(1, empresaId);
            stmtVagas.executeUpdate();
            stmtVagas.close();

            String sqlGetUser = "SELECT ID_USUARIO FROM EMPRESAS WHERE ID_EMPRESA = ?";
            PreparedStatement stmtGetUser = conn.prepareStatement(sqlGetUser);
            stmtGetUser.setInt(1, empresaId);
            ResultSet rs = stmtGetUser.executeQuery();
            Integer usuarioId = null;
            if (rs.next()) {
                usuarioId = rs.getInt("ID_USUARIO");
            }
            rs.close();
            stmtGetUser.close();

            String sqlEmpresa = "DELETE FROM EMPRESAS WHERE ID_EMPRESA = ?";
            PreparedStatement stmtEmpresa = conn.prepareStatement(sqlEmpresa);
            stmtEmpresa.setInt(1, empresaId);
            int affectedRows = stmtEmpresa.executeUpdate();
            stmtEmpresa.close();

            if (usuarioId != null) {
                String sqlUsuario = "DELETE FROM USUARIOS WHERE ID_USUARIO = ?";
                PreparedStatement stmtUsuario = conn.prepareStatement(sqlUsuario);
                stmtUsuario.setInt(1, usuarioId);
                stmtUsuario.executeUpdate();
                stmtUsuario.close();
            }

            conn.commit();
            return affectedRows > 0;
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException("Erro ao realizar rollback: " + ex.getMessage(), ex);
                }
            }
            throw new RuntimeException("Erro ao excluir empresa: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException("Erro ao fechar conexão: " + e.getMessage(), e);
                }
            }
        }
    }
}
