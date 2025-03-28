package infra

import database.ConnectionFactory
import model.Candidato

import java.sql.*

class CandidatoRepository implements ICandidatoRepository {
    ConnectionFactory connectionFactory = new ConnectionFactory(
            'jdbc:postgresql://localhost:5432/linketinder', 'postgres', 'senha123'
    )
    CompetenciaRepository competenciaRepository = new CompetenciaRepository()

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
            } else {
                throw new RuntimeException("Falha ao obter ID_USUARIO gerado.")
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
            } else {
                throw new RuntimeException("Falha ao obter ID_LOCALIZACAO gerado.")
            }
            rsLocalizacao.close()

            String sqlCandidato = """
            INSERT INTO CANDIDATOS (ID_USUARIO, ID_LOCALIZACAO, NOME, SOBRENOME, DATA_NASCIMENTO, CPF)
            VALUES (?, ?, ?, ?, ?, ?) RETURNING ID_CANDIDATO
        """
            PreparedStatement stmtCandidato = conn.prepareStatement(sqlCandidato, Statement.RETURN_GENERATED_KEYS)
            stmtCandidato.setInt(1, usuarioId)
            stmtCandidato.setInt(2, localizacaoId)
            stmtCandidato.setString(3, candidato.nome)
            stmtCandidato.setString(4, candidato.sobrenome)
            stmtCandidato.setDate(5, new java.sql.Date(candidato.dataNascimento.getTime()))
            stmtCandidato.setString(6, candidato.cpf)

            int affectedRows = stmtCandidato.executeUpdate()
            if (affectedRows > 0) {
                ResultSet rsCandidato = stmtCandidato.getGeneratedKeys()
                if (rsCandidato.next()) {
                    candidato.candidatoId = rsCandidato.getInt(1)
                } else {
                    throw new RuntimeException("Falha ao obter ID_CANDIDATO gerado.")
                }
                rsCandidato.close()
            } else {
                throw new RuntimeException("Falha ao inserir candidato no banco de dados.")
            }

            if (candidato.competencias && !candidato.competencias.isEmpty()) {
                candidato.competencias.each { competencia ->
                    String sqlCompetencia = """
                    INSERT INTO CANDIDATO_COMPETENCIAS (ID_CANDIDATO, ID_COMPETENCIA)
                    VALUES (?, ?)
                """
                    PreparedStatement stmtCompetencia = conn.prepareStatement(sqlCompetencia)
                    stmtCompetencia.setInt(1, candidato.candidatoId)
                    stmtCompetencia.setInt(2, competencia.competenciaId)
                    stmtCompetencia.executeUpdate()
                    stmtCompetencia.close()
                }
            } else {
                println("Atenção: Nenhuma competência foi associada ao candidato.")
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
    Candidato listarCandidatoPorId(int candidatoId) {
        String sql = """
        SELECT c.*, u.EMAIL, u.DESCRICAO, l.CEP, l.PAIS 
        FROM CANDIDATOS c
        JOIN USUARIOS u ON c.ID_USUARIO = u.ID_USUARIO
        JOIN LOCALIZACAO l ON c.ID_LOCALIZACAO = l.ID_LOCALIZACAO
        WHERE c.ID_CANDIDATO = ?
    """

        try (Connection conn = connectionFactory.createConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql)
            stmt.setInt(1, candidatoId)
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

                candidato.competencias = competenciaRepository.listarCompetencias(candidato.candidatoId)

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

                candidato.competencias = competenciaRepository.listarCompetencias(candidato.candidatoId)

                candidatos.add(candidato)
            }

            return candidatos
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar candidatos: " + e.getMessage(), e)
        }
    }

    boolean editarCandidato(Candidato candidato) {
        Connection conn = null

        try {
            conn = connectionFactory.createConnection()
            conn.setAutoCommit(false)

            // Atualizar dados básicos do candidato
            String sql = "UPDATE CANDIDATOS SET NOME = ?, SOBRENOME = ?, DATA_NASCIMENTO = ?, CPF = ? WHERE ID_CANDIDATO = ?"
            PreparedStatement stmt = conn.prepareStatement(sql)
            stmt.setString(1, candidato.nome)
            stmt.setString(2, candidato.sobrenome)
            stmt.setDate(3, new java.sql.Date(candidato.dataNascimento.getTime()))
            stmt.setString(4, candidato.cpf)
            stmt.setInt(5, candidato.candidatoId)

            int affectedRows = stmt.executeUpdate()

            if (affectedRows > 0) {
                // Remover todas as competências atuais
                String sqlDelete = "DELETE FROM CANDIDATO_COMPETENCIAS WHERE ID_CANDIDATO = ?"
                PreparedStatement stmtDelete = conn.prepareStatement(sqlDelete)
                stmtDelete.setInt(1, candidato.candidatoId)
                stmtDelete.executeUpdate()
                stmtDelete.close()

                // Adicionar as novas competências
                if (candidato.competencias && !candidato.competencias.isEmpty()) {
                    candidato.competencias.each { competencia ->
                        String sqlCompetencia = "INSERT INTO CANDIDATO_COMPETENCIAS (ID_CANDIDATO, ID_COMPETENCIA) VALUES (?, ?)"
                        PreparedStatement stmtCompetencia = conn.prepareStatement(sqlCompetencia)
                        stmtCompetencia.setInt(1, candidato.candidatoId)
                        stmtCompetencia.setInt(2, competencia.competenciaId)
                        stmtCompetencia.executeUpdate()
                        stmtCompetencia.close()
                    }
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
    boolean excluirCandidato(int candidatoId) {
        Connection conn = null

        try {
            conn = connectionFactory.createConnection()
            conn.setAutoCommit(false)
            String sqlDeleteCompetencias = "DELETE FROM CANDIDATO_COMPETENCIAS WHERE ID_CANDIDATO = ?"
            PreparedStatement stmtDeleteCompetencias = conn.prepareStatement(sqlDeleteCompetencias)
            stmtDeleteCompetencias.setInt(1, candidatoId)
            stmtDeleteCompetencias.executeUpdate()
            stmtDeleteCompetencias.close()

            String sqlGetUser = "SELECT ID_USUARIO FROM CANDIDATOS WHERE ID_CANDIDATO = ?"
            PreparedStatement stmtGetUser = conn.prepareStatement(sqlGetUser)
            stmtGetUser.setInt(1, candidatoId)
            ResultSet rs = stmtGetUser.executeQuery()

            Integer usuarioId = null
            if (rs.next()) {
                usuarioId = rs.getInt("ID_USUARIO")
            }
            rs.close()
            stmtGetUser.close()

            String sqlCandidato = "DELETE FROM CANDIDATOS WHERE ID_CANDIDATO = ?"
            PreparedStatement stmtCandidato = conn.prepareStatement(sqlCandidato)
            stmtCandidato.setInt(1, candidatoId)
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