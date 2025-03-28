package model

class Localizacao {
    int localizacaoId
    String cep
    String pais

    Localizacao(){}
    Localizacao(int localizacaoId, String cep, String pais) {
        this.localizacaoId = localizacaoId
        this.cep = cep
        this.pais = pais
    }
}