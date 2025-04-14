package groovy.model

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

    Candidato(int candidatoId, int usuarioId, int localizacaoId, String nome, String sobrenome, String cpf, Date dataNascimento, List<Competencia> competencias) {
        this.candidatoId = candidatoId
        this.usuarioId = usuarioId
        this.localizacaoId = localizacaoId
        this.nome = nome
        this.sobrenome = sobrenome
        this.cpf = cpf
        this.dataNascimento = dataNascimento
        this.competencias = competencias
    }
}