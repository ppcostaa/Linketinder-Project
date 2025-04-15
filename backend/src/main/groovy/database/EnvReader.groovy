package database

class EnvReader {
    private Properties properties = new Properties()

    EnvReader() {
        this(new File("/home/pcosta/acelera/Linketinder-Project/backend/.env"))
    }

    EnvReader(File envFile) {
        try {
            if (envFile.exists()) {
                properties.load(new FileReader(envFile))
            } else {
                throw new RuntimeException("Arquivo .env não encontrado")
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler arquivo .env: " + e.getMessage(), e)
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