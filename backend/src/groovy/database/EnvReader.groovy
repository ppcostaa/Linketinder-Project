package database

class EnvReader {
    private Properties properties = new Properties()

    EnvReader() {
        def envStream = getClass().getResourceAsStream('/.env')
        if (envStream) {
            try {
                properties.load(new InputStreamReader(envStream, "UTF-8"))
            } catch (Exception e) {
                throw new RuntimeException("Erro ao ler arquivo .env do classpath: " + e.getMessage(), e)
            }
        } else {
            throw new RuntimeException("Arquivo .env não encontrado no classpath")
        }
    }

    String getProperty(String key) {
        String value = properties.getProperty(key)
        if (value == null) {
            throw new RuntimeException("Propriedade '$key' não encontrada no arquivo .env")
        }
        return value
    }
}