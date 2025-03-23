package model

class Vaga {
    int vagaId
    int empresaId
    String descricao
    String titulo
    String cidade
    String estado
    List<String> competenciasRequeridas = []

    Vaga(){}
    Vaga(int vagaId, int empresaId, String titulo, String cidade, String estado, String descricao, List<String> competenciasRequeridas) {
        this.vagaId = vagaId
        this.empresaId = empresaId
        this.titulo = titulo
        this.cidade = cidade
        this.estado = estado
        this.descricao = descricao
        this.competenciasRequeridas = competenciasRequeridas
    }
}