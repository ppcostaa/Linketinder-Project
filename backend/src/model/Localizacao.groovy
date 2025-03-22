package model

class Localizacao {
    int idLocalizacao
    String cep
    String pais

    Localizacao() {}

    Localizacao(Map<String, String> map) {
        this.idLocalizacao = map.idLocalizacao ? map.idLocalizacao.toInteger() : 0
        this.cep = map.cep
        this.pais = map.pais
    }
}