package model

import database.Conexao

class Match {
    int candidatoId
    int empresaId

    Match(int candidatoId, int empresaId) {
        this.candidatoId = candidatoId
        this.empresaId = empresaId
    }

    void salvar() {
        def sql = Conexao.obterConexao()

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

    def verificarMatch(int idCandidato, int idEmpresa) {
        def sql = Conexao.obterConexao()

        if (sql) {
            try {
                def result = sql.firstRow("""
                    SELECT 1
                    FROM LIKES L1
                    JOIN LIKES L2 ON L1.ID_CANDIDATO = L2.ID_EMPRESA AND L1.ID_EMPRESA = L2.ID_CANDIDATO
                    WHERE L1.ID_CANDIDATO = ? AND L1.ID_EMPRESA = ?
                """, [idCandidato, idEmpresa])

                if (result) {
                    println "🔥 Match encontrado entre o candidato e a empresa!"
                    def match = new Match(idCandidato, idEmpresa)
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
