package database


class DatabaseFactory {
    static ConnectionFactory createConnectionFactory() {
        try {
            def envReader = new EnvReader()
            def url = envReader.getProperty('db.url')
            def user = envReader.getProperty('db.user')
            def pass = envReader.getProperty('db.pass')

            return new ConnectionFactory(url, user, pass)
        } catch (Exception e) {
            println "Erro ao criar ConnectionFactory: ${e.message}"
            println "Não foi possível carregar as configurações do banco de dados."
            println "Verifique se o arquivo .env existe e contém as configurações corretas."
            println "O arquivo deve estar em: /home/pcosta/acelera/Linketinder-Project/backend/.env"
            println "O arquivo deve conter: db.url, db.user, db.pass"

            throw new RuntimeException("Falha ao configurar conexão com o banco de dados", e)
        }
    }
}