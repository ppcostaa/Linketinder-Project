package infra

import database.ConnectionFactory
import model.Match

class MatchRepository implements IMatchRepository{
    @Override
    void salvarMatch(Match match) {
        def sql = ConnectionFactory.obterConexao()

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

    @Override
    boolean verificarMatch(int candidatoId, int empresaId) {
        def sql = ConnectionFactory.obterConexao()

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
                    def match = new Match(candidatoId, empresaId)
                    match.salvar()
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
