package model

class Match {
    int matchId
    int candidatoId
    int empresaId
    int vagaId

    Match(){}
    Match(int matchId, int candidatoId, int empresaId, int vagaId) {
        this.matchId = matchId
        this.candidatoId = candidatoId
        this.empresaId = empresaId
        this.vagaId = vagaId
    }
}