package utils

import model.Candidato
import model.Competencia
import model.Localizacao
import model.Usuario

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
    import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class GerenciadorLocalizacao {

        Localizacao create(Localizacao localizacao) {
            String sql = "INSERT INTO LOCALIZACAO (CEP, PAIS) VALUES (?, ?) RETURNING ID_LOCALIZACAO"

            try (Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/linketinder",
                    "postgres",
                    "senha123")
                 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, localizacao.cep)
                stmt.setString(2, localizacao.pais)

                int affectedRows = stmt.executeUpdate()

                if (affectedRows > 0) {
                    ResultSet rs = stmt.getGeneratedKeys()
                    if (rs.next()) {
                        localizacao.idLocalizacao = rs.getInt(1)
                    }
                    rs.close()
                }

                return localizacao
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao criar localização: " + e.getMessage(), e)
            }

    }
    List<Localizacao> findAll() {
        String sql = "SELECT * FROM LOCALIZACAO"
        List<Localizacao> localizacoes = []

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/linketinder",
                "postgres",
                "senha123")
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Localizacao localizacao = new Localizacao()
                localizacao.idLocalizacao = rs.getInt("ID_LOCALIZACAO")
                localizacao.cep = rs.getString("CEP")
                localizacao.pais = rs.getString("PAIS")
                localizacoes.add(localizacao)
            }

            return localizacoes
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar localizações: " + e.getMessage(), e)
        }
    }
    Localizacao findById(Integer id) {
        String sql = "SELECT * FROM LOCALIZACAO WHERE ID_LOCALIZACAO = ?"

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/linketinder",
                "postgres",
                "senha123")
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id ?: null)
            ResultSet rs = stmt.executeQuery()

            if (rs.next()) {
                Localizacao localizacao = new Localizacao()
                localizacao.idLocalizacao = rs.getInt("ID_LOCALIZACAO")
                localizacao.cep = rs.getString("CEP")
                localizacao.pais = rs.getString("PAIS")
                return localizacao
            }

            return null
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário: " + e.getMessage(), e)
        }
    }
    boolean update(Localizacao localizacao, int idCandidato) {
        Connection conn = null

        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/linketinder",
                    "postgres",
                    "senha123")
            conn.setAutoCommit(false)

            String sql = "UPDATE Localizacao SET CEP = ?, PAIS = ? WHERE ID_LOCALIZACAO = ?"
            PreparedStatement stmt = conn.prepareStatement(sql)
            stmt.setString(1, localizacao.cep)
            stmt.setString(2, localizacao.pais)
            stmt.setInt(3, localizacao.idLocalizacao)

            int affectedRows = stmt.executeUpdate()

            if (affectedRows > 0) {
                conn.commit()
                return affectedRows > 0
            } else {
                throw new RuntimeException("Nenhuma localização foi atualizada.")
            }

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
