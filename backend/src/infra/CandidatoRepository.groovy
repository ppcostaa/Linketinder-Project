package infra

import database.ConnectionFactory
import model.Candidato
import model.Competencia

import java.sql.*

class CandidatoRepository implements ICandidatoRepository {
    ConnectionFactory connectionFactory = new ConnectionFactory(
            'jdbc:postgresql://localhost:5432/linketinder', 'postgres', 'senha123'
    )

    @Override
    Candidato salvarCandidato(Candidato candidato, String email, String senha, String descricao, String cep, String pais) {
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

            String sql = "INSERT INTO CANDIDATOS (ID_USUARIO, ID_LOCALIZACAO, NOME, SOBRENOME, DATA_NASCIMENTO, CPF) VALUES (?, ?, ?, ?, ?, ?) RETURNING ID_CANDIDATO"
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            stmt.setInt(1, usuarioId)
            stmt.setInt(2, localizacaoId)
            stmt.setString(3, candidato.nome)
            stmt.setString(4, candidato.sobrenome)
            stmt.setDate(5, new java.sql.Date(candidato.dataNascimento.getTime()))
            stmt.setString(6, candidato.cpf)

            int affectedRows = stmt.executeUpdate()

            if (affectedRows > 0) {
                ResultSet rsCandidato = stmt.getGeneratedKeys()
                if (rsCandidato.next()) {
                    candidato.candidatoId = rsCandidato.getInt(1)
                }
                rsCandidato.close()
            }

            CompetenciaRepository competenciaRepository = new CompetenciaRepository()
            candidato.competencias = candidato.competencias.collect { competencia ->
                new Competencia(competenciaNome: competencia)
            }

            candidato.competencias.each { competencia ->
                Competencia competenciaExiste = competenciaRepository.findByName(competencia.nomeCompetencia)

                if (!competenciaExiste) {
                    competencia = competenciaRepository.salvarCompetencia(competencia)
                } else {
                    competencia.competenciaId = competenciaExiste.competenciaId
                }

                String sqlCompetencia = "INSERT INTO CANDIDATO_COMPETENCIAS (ID_CANDIDATO, ID_COMPETENCIA) VALUES (?, ?)"
                PreparedStatement stmtCompetencia = conn.prepareStatement(sqlCompetencia)
                stmtCompetencia.setInt(1, candidato.candidatoId)
                stmtCompetencia.setInt(2, competencia.competenciaId)
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

    @Override
    Candidato listarCandidatoPorId(int id) {
        String sql = """
            SELECT c.*, u.EMAIL, u.DESCRICAO, l.CEP, l.PAIS 
            FROM CANDIDATOS c
            JOIN USUARIOS u ON c.ID_USUARIO = u.ID_USUARIO
            JOIN LOCALIZACAO l ON c.ID_LOCALIZACAO = l.ID_LOCALIZACAO
            WHERE c.ID_CANDIDATO = ?
        """

        try (Connection conn = connectionFactory.createConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql)

            stmt.setInt(1, id)
            ResultSet rs = stmt.executeQuery()

            if (rs.next()) {
                Candidato candidato = new Candidato()
                candidato.candidatoId = rs.getInt("ID_CANDIDATO")
                candidato.usuarioId = rs.getInt("ID_USUARIO")
                candidato.localizacaoId = rs.getInt("ID_LOCALIZACAO")
                candidato.nome = rs.getString("NOME")
                candidato.sobrenome = rs.getString("SOBRENOME")
                candidato.dataNascimento = rs.getDate("DATA_NASCIMENTO")
                candidato.cpf = rs.getString("CPF")

                candidato.competencias = CompetenciaRepository.listarCompetencias(candidato.candidatoId)

                return candidato
            }

            return null
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar candidato: " + e.getMessage(), e)
        }
    }

    @Override
    List<Candidato> listarCandidatos() {
        String sql = """
            SELECT c.*, u.EMAIL, u.DESCRICAO, l.CEP, l.PAIS 
            FROM CANDIDATOS c
            JOIN USUARIOS u ON c.ID_USUARIO = u.ID_USUARIO
            JOIN LOCALIZACAO l ON c.ID_LOCALIZACAO = l.ID_LOCALIZACAO
        """

        List<Candidato> candidatos = []

        try (Connection conn = connectionFactory.createConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql)
            ResultSet rs = stmt.executeQuery()

            while (rs.next()) {
                Candidato candidato = new Candidato()
                candidato.candidatoId = rs.getInt("ID_CANDIDATO")
                candidato.usuarioId = rs.getInt("ID_USUARIO")
                candidato.localizacaoId = rs.getInt("ID_LOCALIZACAO")
                candidato.nome = rs.getString("NOME")
                candidato.sobrenome = rs.getString("SOBRENOME")
                candidato.dataNascimento = rs.getDate("DATA_NASCIMENTO")
                candidato.cpf = rs.getString("CPF")

                candidato.competencias = CompetenciaRepository.listarCompetencias(candidato.idCandidato)

                candidatos.add(candidato)
            }

            return candidatos
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar candidatos: " + e.getMessage(), e)
        }
    }

    @Override
    boolean editarCandidato(Candidato candidato) {
        Connection conn = null

        try {
            conn = connectionFactory.createConnection()
            conn.setAutoCommit(false)

            String sql = "UPDATE CANDIDATOS SET NOME = ?, SOBRENOME = ?, DATA_NASCIMENTO = ?, CPF = ? WHERE ID_CANDIDATO = ?"
            PreparedStatement stmt = conn.prepareStatement(sql)
            stmt.setString(1, candidato.nome)
            stmt.setString(2, candidato.sobrenome)
            stmt.setDate(3, new java.sql.Date(candidato.dataNascimento.getTime()))
            stmt.setString(4, candidato.cpf)
            stmt.setInt(5, candidato.candidatoId)

            int affectedRows = stmt.executeUpdate()

            if (affectedRows > 0 && candidato.competencias) {
                String sqlDelete = "DELETE FROM CANDIDATO_COMPETENCIAS WHERE ID_CANDIDATO = ?"
                PreparedStatement stmtDelete = conn.prepareStatement(sqlDelete)
                stmtDelete.setInt(1, candidato.candidatoId)
                stmtDelete.executeUpdate()
                stmtDelete.close()

                CompetenciaRepository competenciaRepository = new CompetenciaRepository()

                candidato.competencias = candidato.competencias.collect { competencia ->
                    new Competencia(competenciaNome: competencia)
                }

                candidato.competencias.each { competencia ->
                    Competencia competenciasExistentes = competenciaRepository.findByName(competencia.nomeCompetencia)
                    if (!competenciasExistentes) {
                        competencia = competenciaRepository.create(competencia)
                    } else {
                        competencia.competenciaId = competenciasExistentes.competenciaId
                    }

                    String sqlCompetencia = "INSERT INTO CANDIDATO_COMPETENCIAS (ID_CANDIDATO, ID_COMPETENCIA) VALUES (?, ?)"
                    PreparedStatement stmtCompetencia = conn.prepareStatement(sqlCompetencia)
                    stmtCompetencia.setInt(1, candidato.candidatoId)
                    stmtCompetencia.setInt(2, competencia.competenciaId)
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

    @Override
    boolean excluirCandidato(int id) {
        Connection conn = null

        try {
            conn = connectionFactory.createConnection()
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
            int usuarioId = null
            if (rs.next()) {
                usuarioId = rs.getInt("ID_USUARIO")
            }
            rs.close()
            stmtGetUser.close()

            String sqlCandidato = "DELETE FROM CANDIDATOS WHERE ID_CANDIDATO = ?"
            PreparedStatement stmtCandidato = conn.prepareStatement(sqlCandidato)
            stmtCandidato.setInt(1, id)
            int affectedRows = stmtCandidato.executeUpdate()
            stmtCandidato.close()

            if (usuarioId != null) {
                String sqlUsuario = "DELETE FROM USUARIOS WHERE ID_USUARIO = ?"
                PreparedStatement stmtUsuario = conn.prepareStatement(sqlUsuario)
                stmtUsuario.setInt(1, usuarioId)
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
