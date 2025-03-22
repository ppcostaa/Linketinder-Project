package model

class Candidato {
    final int candidatoId
    final int usuarioId
    final int localizacaoId
    String nome
    String sobrenome
    Date dataNascimento
    String cpf
    List<Competencia> competencias = []

    Candidato(int candidatoId, int usuarioId, int localizacaoId, String nome, String sobrenome, Date dataNascimento, String cpf, List<Competencia> competencias) {
        this.candidatoId = candidatoId
        this.usuarioId = usuarioId
        this.localizacaoId = localizacaoId
        this.nome = nome
        this.sobrenome = sobrenome
        this.dataNascimento = dataNascimento
        this.cpf = cpf
        this.competencias = competencias
    }
}