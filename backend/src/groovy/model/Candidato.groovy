package model

class Candidato {
    int candidatoId
    int usuarioId
    int localizacaoId
    String nome
    String sobrenome
    Date dataNascimento
    String cpf
    List<Competencia> competencias = []

    Candidato() {}

}