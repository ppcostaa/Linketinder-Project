package utils

import model.Candidato
import model.Competencia

import java.sql.*

class GerenciadorCandidato {

    Candidato create(Candidato candidato, String email, String senha, String descricao, String cep, String pais) {
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

            String sql = "INSERT INTO CANDIDATOS (ID_USUARIO, ID_LOCALIZACAO, NOME, SOBRENOME, DATA_NASCIMENTO, CPF) VALUES (?, ?, ?, ?, ?, ?) RETURNING ID_CANDIDATO"
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            stmt.setInt(1, idUsuario)
            stmt.setInt(2, idLocalizacao)
            stmt.setString(3, candidato.nome)
            stmt.setString(4, candidato.sobrenome)
            stmt.setDate(5, new java.sql.Date(candidato.dataNascimento.getTime()))
            stmt.setString(6, candidato.cpf)

            int affectedRows = stmt.executeUpdate()

            if (affectedRows > 0) {
                ResultSet rsCandidato = stmt.getGeneratedKeys()
                if (rsCandidato.next()) {
                    candidato.idCandidato = rsCandidato.getInt(1)
                }
                rsCandidato.close()
            }

            GerenciadorCompetencia gerenciadorCompetencia = new GerenciadorCompetencia()
            candidato.competencias = candidato.competencias.collect { competencia ->
                new Competencia(nomeCompetencia: competencia)
            }

            candidato.competencias.each { competencia ->
                Competencia existingCompetencia = gerenciadorCompetencia.findByName(competencia.nomeCompetencia)

                if (!existingCompetencia) {
                    competencia = gerenciadorCompetencia.create(competencia)
                } else {
                    competencia.idCompetencia = existingCompetencia.idCompetencia
                }

                String sqlCompetencia = "INSERT INTO CANDIDATO_COMPETENCIAS (ID_CANDIDATO, ID_COMPETENCIA) VALUES (?, ?)"
                PreparedStatement stmtCompetencia = conn.prepareStatement(sqlCompetencia)
                stmtCompetencia.setInt(1, candidato.idCandidato)
                stmtCompetencia.setInt(2, competencia.idCompetencia)
                stmtCompetencia.executeUpdate()
                stmtCompetencia.close()
            }

            conn.commit()
            return candidato
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback()
                } catch (SQLException ex) {
                    throw new RuntimeException("Erro ao realizar rollback: " + ex.getMessage(), ex)
                }
            }
            throw new RuntimeException("Erro ao criar candidato: " + e.getMessage(), e)
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

    List<Competencia> findCompetencias(Integer idCandidato) {
        String sql = """
            SELECT c.* FROM COMPETENCIAS c
            JOIN CANDIDATO_COMPETENCIAS cc ON cc.ID_COMPETENCIA = c.ID_COMPETENCIA
            WHERE cc.ID_CANDIDATO = ?
        """

        List<Competencia> competencias = []

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/linketinder",
                "postgres",
                "senha123")
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCandidato)
            ResultSet rs = stmt.executeQuery()

            while (rs.next()) {
                Competencia competencia = new Competencia()
                competencia.idCompetencia = rs.getInt("ID_COMPETENCIA")
                competencia.nomeCompetencia = rs.getString("NOME_COMPETENCIA")
                competencias.add(competencia)
            }

            return competencias
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar competências do candidato: " + e.getMessage(), e)
        }
    }

    Candidato findById(Integer id) {
        String sql = """
            SELECT c.*, u.EMAIL, u.DESCRICAO, l.CEP, l.PAIS 
            FROM CANDIDATOS c
            JOIN USUARIOS u ON c.ID_USUARIO = u.ID_USUARIO
            JOIN LOCALIZACAO l ON c.ID_LOCALIZACAO = l.ID_LOCALIZACAO
            WHERE c.ID_CANDIDATO = ?
        """

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/linketinder",
                "postgres",
                "senha123")
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id)
            ResultSet rs = stmt.executeQuery()

            if (rs.next()) {
                Candidato candidato = new Candidato()
                candidato.idCandidato = rs.getInt("ID_CANDIDATO")
                candidato.idUsuario = rs.getInt("ID_USUARIO")
                candidato.idLocalizacao = rs.getInt("ID_LOCALIZACAO")
                candidato.nome = rs.getString("NOME")
                candidato.sobrenome = rs.getString("SOBRENOME")
                candidato.dataNascimento = rs.getDate("DATA_NASCIMENTO")
                candidato.cpf = rs.getString("CPF")

                candidato.competencias = findCompetencias(candidato.idCandidato)

                return candidato
            }

            return null
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar candidato: " + e.getMessage(), e)
        }
    }

    List<Candidato> findAll() {
        String sql = """
            SELECT c.*, u.EMAIL, u.DESCRICAO, l.CEP, l.PAIS 
            FROM CANDIDATOS c
            JOIN USUARIOS u ON c.ID_USUARIO = u.ID_USUARIO
            JOIN LOCALIZACAO l ON c.ID_LOCALIZACAO = l.ID_LOCALIZACAO
        """

        List<Candidato> candidatos = []

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/linketinder",
                "postgres",
                "senha123")
             PreparedStatement stmt = conn.prepareStatement(sql)
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Candidato candidato = new Candidato()
                candidato.idCandidato = rs.getInt("ID_CANDIDATO")
                candidato.idUsuario = rs.getInt("ID_USUARIO")
                candidato.idLocalizacao = rs.getInt("ID_LOCALIZACAO")
                candidato.nome = rs.getString("NOME")
                candidato.sobrenome = rs.getString("SOBRENOME")
                candidato.dataNascimento = rs.getDate("DATA_NASCIMENTO")
                candidato.cpf = rs.getString("CPF")

                candidato.competencias = findCompetencias(candidato.idCandidato)

                candidatos.add(candidato)
            }

            return candidatos
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar candidatos: " + e.getMessage(), e)
        }
    }

    boolean update(Candidato candidato) {
        Connection conn = null

        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/linketinder",
                    "postgres",
                    "senha123")
            conn.setAutoCommit(false)

            String sql = "UPDATE CANDIDATOS SET NOME = ?, SOBRENOME = ?, DATA_NASCIMENTO = ?, CPF = ? WHERE ID_CANDIDATO = ?"
            PreparedStatement stmt = conn.prepareStatement(sql)
            stmt.setString(1, candidato.nome)
            stmt.setString(2, candidato.sobrenome)
            stmt.setDate(3, new java.sql.Date(candidato.dataNascimento.getTime()))
            stmt.setString(4, candidato.cpf)
            stmt.setInt(5, candidato.idCandidato)

            int affectedRows = stmt.executeUpdate()

            if (affectedRows > 0 && candidato.competencias) {
                String sqlDelete = "DELETE FROM CANDIDATO_COMPETENCIAS WHERE ID_CANDIDATO = ?"
                PreparedStatement stmtDelete = conn.prepareStatement(sqlDelete)
                stmtDelete.setInt(1, candidato.idCandidato)
                stmtDelete.executeUpdate()
                stmtDelete.close()

                GerenciadorCompetencia gerenciadorCompetencia = new GerenciadorCompetencia()

                candidato.competencias = candidato.competencias.collect { competencia ->
                    new Competencia(nomeCompetencia: competencia)
                }

                candidato.competencias.each { competencia ->
                    Competencia competenciasExistentes = gerenciadorCompetencia.findByName(competencia.nomeCompetencia)
                    if (!competenciasExistentes) {
                        competencia = gerenciadorCompetencia.create(competencia)
                    } else {
                        competencia.idCompetencia = competenciasExistentes.idCompetencia
                    }

                    String sqlCompetencia = "INSERT INTO CANDIDATO_COMPETENCIAS (ID_CANDIDATO, ID_COMPETENCIA) VALUES (?, ?)"
                    PreparedStatement stmtCompetencia = conn.prepareStatement(sqlCompetencia)
                    stmtCompetencia.setInt(1, candidato.idCandidato)
                    stmtCompetencia.setInt(2, competencia.idCompetencia)
                    stmtCompetencia.executeUpdate()
                    stmtCompetencia.close()
                }
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
            throw new RuntimeException("Erro ao atualizar candidato: " + e.getMessage(), e)
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

    boolean delete(Integer id) {
        Connection conn = null

        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/linketinder",
                    "postgres",
                    "senha123")
            conn.setAutoCommit(false)

            String sqlCompetencias = "DELETE FROM CANDIDATO_COMPETENCIAS WHERE ID_CANDIDATO = ?"
            PreparedStatement stmtCompetencias = conn.prepareStatement(sqlCompetencias)
            stmtCompetencias.setInt(1, id)
            stmtCompetencias.executeUpdate()
            stmtCompetencias.close()

            String sqlGetUser = "SELECT ID_USUARIO FROM CANDIDATOS WHERE ID_CANDIDATO = ?"
            PreparedStatement stmtGetUser = conn.prepareStatement(sqlGetUser)
            stmtGetUser.setInt(1, id)
            ResultSet rs = stmtGetUser.executeQuery()
            Integer idUsuario = null
            if (rs.next()) {
                idUsuario = rs.getInt("ID_USUARIO")
            }
            rs.close()
            stmtGetUser.close()

            String sqlCandidato = "DELETE FROM CANDIDATOS WHERE ID_CANDIDATO = ?"
            PreparedStatement stmtCandidato = conn.prepareStatement(sqlCandidato)
            stmtCandidato.setInt(1, id)
            int affectedRows = stmtCandidato.executeUpdate()
            stmtCandidato.close()

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
            throw new RuntimeException("Erro ao excluir candidato: " + e.getMessage(), e)
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