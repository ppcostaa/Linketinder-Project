package model

import database.ConnectionFactory
class Like {
    int idCandidato
    int idEmpresa

    Like(int idCandidato = null, int idEmpresa = null) {
        this.idCandidato = idCandidato
        this.idEmpresa = idEmpresa
    }
    void salvar() {
        def sql = ConnectionFactory.obterConexao()

        if (sql) {
            try {
                def likeExistente = sql.firstRow("SELECT 1 FROM LIKES WHERE ID_CANDIDATO = ? AND ID_EMPRESA = ?", [idCandidato, idEmpresa])

                if (!likeExistente) {
                    if (idCandidato != null && idEmpresa == null) {
                        sql.executeInsert("INSERT INTO LIKES (ID_CANDIDATO, ID_EMPRESA) VALUES (?, ?)", [idCandidato, idEmpresa])
                    } else if (idCandidato == null && idEmpresa != null) {
                        sql.executeInsert("INSERT INTO LIKES (ID_CANDIDATO, ID_EMPRESA) VALUES (?, ?)", [idCandidato, idEmpresa])
                    }
                    println "🩵 Like!"
                } else {
                    println "❌ Like já registrado anteriormente."
                }
            } catch (Exception e) {
                println "❌ Erro ao salvar o like: ${e.message}"
            } finally {
                sql.close()
            }
        } else {
            println "❌ Conexão não foi estabelecida."
        }
    }

}
