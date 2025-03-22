package model

class Vaga extends Empresa {
    int vagaId
    String vagaNome
    String descricaoVaga
    String estado
    String cidade
    List<String> competencias

    Vaga(int empresaId, int vagaId, String vagaNome, String descricaoVaga, String estado, String cidade, List<String> competencias
    ) {
        super(empresaId)
        this.vagaId = vagaId
        this.vagaNome = vagaNome
        this.descricaoVaga = descricaoVaga
        this.estado = estado
        this.cidade = cidade
        this.competencias = competencias
    }
}

