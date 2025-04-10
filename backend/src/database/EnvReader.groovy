package database

class EnvReader {
    private final Properties properties = new Properties()

    EnvReader() {
        String envPath = "/home/pcosta/acelera/Linketinder-Project/backend/.env"
        File envFile = new File(envPath)

        if (!envFile.exists()) {
            throw new FileNotFoundException("Arquivo .env não encontrado")
        }

        try (InputStream input = new FileInputStream(envFile)) {
            properties.load(new StringReader(envFile.text.replace('=', ':')))
        }

        // Verificação manual das propriedades necessárias
        if (!properties.containsKey('db.url') ||
                !properties.containsKey('db.user') ||
                !properties.containsKey('db.pass')) {
            throw new IllegalStateException("Propriedades do banco de dados faltando no .env")
        }
    }

    String getProperty(String key) {
        return properties.getProperty(key)
    }
}