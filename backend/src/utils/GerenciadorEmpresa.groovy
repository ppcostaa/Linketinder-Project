package utils

import model.Empresa
import model.Localizacao
import model.Usuario
import model.Vaga

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class GerenciadorEmpresa {

    Empresa create(Empresa empresa, String email, String senha, String descricao, String cep, String pais) {
        Connection conn = null
        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/linketinder",
                    "postgres",
                    "senha123")
            conn.setAutoCommit(false)

            String sqlUsuario = "INSERT INTO USUARIOS (EMAIL, SENHA, DESCRICAO) VALUES (?, ?, ?) RETURNING ID_USUARIO"
            PreparedStatement stmtUsuario = conn.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)
            stmtUsuario.setString(1, email)
            stmtUsuario.setString(2, senha)
            stmtUsuario.setString(3, descricao)
            stmtUsuario.executeUpdate()

            ResultSet rsUsuario = stmtUsuario.getGeneratedKeys()
            int idUsuario = 0
            if (rsUsuario.next()) {
                idUsuario = rsUsuario.getInt(1)
            }
            rsUsuario.close()

            String sqlLocalizacao = "INSERT INTO LOCALIZACAO (CEP, PAIS) VALUES (?, ?) RETURNING ID_LOCALIZACAO"
            PreparedStatement stmtLocalizacao = conn.prepareStatement(sqlLocalizacao, Statement.RETURN_GENERATED_KEYS)
            stmtLocalizacao.setString(1, cep)
            stmtLocalizacao.setString(2, pais)
            stmtLocalizacao.executeUpdate()

            ResultSet rsLocalizacao = stmtLocalizacao.getGeneratedKeys()
            int idLocalizacao = 0
            if (rsLocalizacao.next()) {
                idLocalizacao = rsLocalizacao.getInt(1)
            }
            rsLocalizacao.close()

            String sql = "INSERT INTO EMPRESAS (ID_USUARIO, ID_LOCALIZACAO, NOME_EMPRESA, CNPJ) VALUES (?, ?, ?, ?) RETURNING ID_EMPRESA"
GerenciadorLocalizacao
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            stmt.setInt(1, empresa.idUsuario)
            stmt.setInt(2, empresa.idLocalizacao)
            stmt.setString(3, empresa.nomeEmpresa)
            stmt.setString(4, empresa.cnpj)

            int affectedRows = stmt.executeUpdate()

            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys()
                if (rs.next()) {
                    empresa.idEmpresa = rs.getInt(1)
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

    List<Vaga> findVagas(Integer idEmpresa) {
        String sql = "SELECT * FROM VAGAS WHERE ID_EMPRESA = ?"

        List<Vaga> vagas = []

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/linketinder",
                "postgres",
                "senha123")
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEmpresa)
            ResultSet rs = stmt.executeQuery()

            while (rs.next()) {
                Vaga vaga = new Vaga()
                vaga.idVaga = rs.getInt("ID_VAGA")
                vaga.idEmpresa = rs.getInt("ID_EMPRESA")
                vaga.nomeVaga = rs.getString("NOME_VAGA")
                vaga.descricaoVaga = rs.getString("DESCRICAO_VAGA")
                vaga.localEstado = rs.getString("LOCAL_ESTADO")
                vaga.localCidade = rs.getString("LOCAL_CIDADE")
                vagas.add(vaga)
            }

            return vagas
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar vagas da empresa: " + e.getMessage(), e)
        }
    }

    Empresa findById(Integer id) {
        String sql = """
            SELECT e.*, u.EMAIL, u.DESCRICAO, l.CEP, l.PAIS 
            FROM EMPRESAS e
            JOIN USUARIOS u ON e.ID_USUARIO = u.ID_USUARIO
            JOIN LOCALIZACAO l ON e.ID_LOCALIZACAO = l.ID_LOCALIZACAO
            WHERE e.ID_EMPRESA = ?
        """

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/linketinder",
                "postgres",
                "senha123")
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id)
            ResultSet rs = stmt.executeQuery()

            if (rs.next()) {
                Empresa empresa = new Empresa()
                empresa.idEmpresa = rs.getInt("ID_EMPRESA")
                empresa.idUsuario = rs.getInt("ID_USUARIO")
                empresa.idLocalizacao = rs.getInt("ID_LOCALIZACAO")
                empresa.nomeEmpresa = rs.getString("NOME_EMPRESA")
                empresa.cnpj = rs.getString("CNPJ")

                empresa.vagas = findVagas(empresa.idEmpresa)

                return empresa
            }

            return null
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar empresa: " + e.getMessage(), e)
        }
    }
    List<Empresa> findAll() {
        String sql = """
            SELECT e.*, u.EMAIL, u.DESCRICAO, l.CEP, l.PAIS 
            FROM EMPRESAS e
            JOIN USUARIOS u ON e.ID_USUARIO = u.ID_USUARIO
            JOIN LOCALIZACAO l ON e.ID_LOCALIZACAO = l.ID_LOCALIZACAO
        """

        List<Empresa> empresas = []

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/linketinder",
                "postgres",
                "senha123")
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Empresa empresa = new Empresa()
                empresa.idEmpresa = rs.getInt("ID_EMPRESA")
                empresa.idUsuario = rs.getInt("ID_USUARIO")
                empresa.idLocalizacao = rs.getInt("ID_LOCALIZACAO")
                empresa.nomeEmpresa = rs.getString("NOME_EMPRESA")
                empresa.cnpj = rs.getString("CNPJ")

                empresa.vagas = findVagas(empresa.idEmpresa)

                empresas.add(empresa)
            }

            return empresas
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar empresas: " + e.getMessage(), e)
        }
    }

    boolean update(Empresa empresa) {
        String sql = "UPDATE EMPRESAS SET NOME_EMPRESA = ?, CNPJ = ? WHERE ID_EMPRESA = ?"

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/linketinder",
                "postgres",
                "senha123")
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, empresa.nomeEmpresa)
            stmt.setString(2, empresa.cnpj)
            stmt.setInt(3, empresa.idEmpresa)

            int affectedRows = stmt.executeUpdate()
            return affectedRows > 0
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar empresa: " + e.getMessage(), e)
        }
    }

    boolean delete(Integer id) {
        Connection conn = null

        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/linketinder",
                    "postgres",
                    "senha123")
            conn.setAutoCommit(false)

            String sqlVagas = "DELETE FROM VAGAS WHERE ID_EMPRESA = ?"
            PreparedStatement stmtVagas = conn.prepareStatement(sqlVagas)
            stmtVagas.setInt(1, id)
            stmtVagas.executeUpdate()
            stmtVagas.close()

            String sqlGetUser = "SELECT ID_USUARIO FROM EMPRESAS WHERE ID_EMPRESA = ?"
            PreparedStatement stmtGetUser = conn.prepareStatement(sqlGetUser)
            stmtGetUser.setInt(1, id)
            ResultSet rs = stmtGetUser.executeQuery()
            Integer idUsuario = null
            if (rs.next()) {
                idUsuario = rs.getInt("ID_USUARIO")
            }
            rs.close()
            stmtGetUser.close()

            String sqlEmpresa = "DELETE FROM EMPRESAS WHERE ID_EMPRESA = ?"
            PreparedStatement stmtEmpresa = conn.prepareStatement(sqlEmpresa)
            stmtEmpresa.setInt(1, id)
            int affectedRows = stmtEmpresa.executeUpdate()
            stmtEmpresa.close()

            if (idUsuario != null) {
                String sqlUsuario = "DELETE FROM USUARIOS WHERE ID_USUARIO = ?"
                PreparedStatement stmtUsuario = conn.prepareStatement(sqlUsuario)
                stmtUsuario.setInt(1, idUsuario)
                stmtUsuario.executeUpdate()
                stmtUsuario.close()
            }

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
            throw new RuntimeException("Erro ao excluir empresa: " + e.getMessage(), e)
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