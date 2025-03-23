package model

class Like {
    int likeId
    int candidatoId
    int vagaId
    int empresaId

    Like(){}
    Like(int likeId, int candidatoId, int vagaId, int empresaId) {
        this.likeId = likeId
        this.candidatoId = candidatoId
        this.vagaId = vagaId
        this.empresaId = empresaId
    }
}