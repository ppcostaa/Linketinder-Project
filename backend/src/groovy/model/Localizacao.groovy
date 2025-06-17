package model

class Localizacao {
    int localizacaoId
    String cep
    String pais

    Localizacao() {}

    @Override
    String toString() {
        return "Localizacao(id: $localizacaoId, cep: $cep, pais: $pais)"
    }
}