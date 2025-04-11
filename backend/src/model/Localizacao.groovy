package model

class Localizacao {
    int localizacaoId
    String cep
    String pais

    Localizacao() {}

    Localizacao(int localizacaoId, String cep, String pais) {
        this.localizacaoId = localizacaoId
        this.cep = cep
        this.pais = pais
    }

    Localizacao(String cep, String pais) {
        this.cep = cep
        this.pais = pais
    }

    boolean dadosValidos() {
        return cep?.trim() && pais?.trim()
    }

    @Override
    String toString() {
        return "Localizacao(id: $localizacaoId, cep: $cep, pais: $pais)"
    }
}