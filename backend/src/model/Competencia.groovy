package model

class Competencia {
    Integer idCompetencia
    String nomeCompetencia
    Competencia() {}

    Competencia(Map<String, String> map) {
        this.idCompetencia = map.idCompetencia ? map.idCompetencia.toInteger() : 0
        this.nomeCompetencia = map.nomeCompetencia
    }
}