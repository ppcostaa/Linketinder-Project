package infra

import database.ConnectionFactory
import model.Match

import java.sql.*

class MatchRepository implements IMatchRepository {
    @Override
    void salvarMatch(Match match) {
        def sql = ConnectionFactory.createConnection()

        if (sql) {
            try {
                sql.executeInsert("INSERT INTO MATCHS (CANDIDATO_ID, EMPRESA_ID) VALUES (?, ?)", [candidatoId, empresaId])
                println "🔥 Deu Match!"
            } catch (Exception e) {
                println "❌ Erro ao salvar o match: ${e.message}"
            } finally {
                sql.close()
            }
        } else {
            println "❌ Conexão não foi estabelecida."
        }
    }

//    @Override
//    List<Match> listarMatchs() {
//        String sql = "SELECT * FROM MATCHS"
//        List<Match> matchs = []
//        try (Connection conn = DriverManager.getConnection(
//                "jdbc:postgresql://localhost:5432/linketinder",
//                "postgres",
//                "senha123")
//             PreparedStatement stmt = conn.prepareStatement(sql);
//             ResultSet rs = stmt.executeQuery()) {
//
//            while (rs.next()) {
//                Match match = new Match()
//                match.candidatoId = rs.getInt("ID_CANDIDATO")
//                match.empresaId = rs.getString("ID_EMPRESA")
//                matchs.add(match)
//            }
//
//            return matchs
//        } catch (SQLException e) {
//            throw new RuntimeException("Erro ao listar matchs: " + e.getMessage(), e)
//        }
//    }

    @Override
    boolean verificarMatch(int candidatoId, int empresaId) {
        def sql = ConnectionFactory.createConnection()

        if (sql) {
            try {
                def result = sql.firstRow("""
                    SELECT 1
                    FROM LIKES L1
                    JOIN LIKES L2 ON L1.ID_CANDIDATO = L2.ID_EMPRESA AND L1.ID_EMPRESA = L2.ID_CANDIDATO
                    WHERE L1.ID_CANDIDATO = ? AND L1.ID_EMPRESA = ?
                """, [candidatoId, empresaId])

                if (result) {
                    println "🔥 Match encontrado entre o candidato e a empresa!"
                    salvarMatch()
                } else {
                    return
                }
            } catch (Exception e) {
                println "❌ Erro ao verificar o match: ${e.message}"
            } finally {
                sql.close()
            }
        } else {
            println "❌ Conexão não foi estabelecida."
        }
    }
}
