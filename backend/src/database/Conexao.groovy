package database
import groovy.sql.Sql
class Conexao {

    def url = 'jdbc:postgresql://localhost:5432/linketinder'
    def user = 'postgres'
    def password = 'senha123'
    def driver = 'org.postgresql.Driver'

    def obterConexao() {
        try {
            def sql = Sql.newInstance(url, user, password, driver)
            println "✅ Conexão bem-sucedida!"

            sql.eachRow('SELECT * FROM Candidatos') { row ->
                println "Usuário: ${row.nome}"
            }

            sql.close()
        } catch (Exception e) {
            println "❌ Erro ao conectar ao banco de dados: ${e.message}"
        }
    }
}

