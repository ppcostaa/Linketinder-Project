package model

class Localizacao {
    final int localizacaoId
    String cep
    String pais

    Localizacao(int localizacaoId, String cep, String pais) {
        this.localizacaoId = localizacaoId
        this.cep = cep
        this.pais = pais
    }
}