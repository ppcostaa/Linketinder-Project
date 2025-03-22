package model

class Candidato {
    int idCandidato
    int idUsuario
    int idLocalizacao
    String nome
    String sobrenome
    Date dataNascimento
    String cpf
    List<Competencia> competencias = []

    Candidato() {}

    Candidato(Map<String, String> map) {
        this.idCandidato = map.idCandidato ? map.idCandidato.toInteger() : 0
        this.idLocalizacao = map.idLocalizacao ? map.idLocalizacao.toInteger() : 0
        this.idUsuario = map.idUsuario ? map.idUsuario.toInteger() : 0
        this.nome = map.nome
        this.sobrenome = map.sobrenome
        this.cpf = map.cpf
        this.dataNascimento = map.dataNascimento
        this.competencias = map.competencias
    }
}