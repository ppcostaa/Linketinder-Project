package model

class Vaga {
    int vagaId
    int empresaId
    String descricao
    String titulo
    String cidade
    String estado
    List<Competencia> competencias = []

    Vaga(){}
}