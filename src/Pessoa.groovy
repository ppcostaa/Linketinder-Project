abstract class Pessoa implements IPessoa {
    String nome
    String email
    String estado
    String cep
    String descricao
    List<String> competencias = ["Java", "Python", "Typescript", "Banco de dados", "Angular", "React", "Spring"]

    Pessoa(String nome, String email, String estado, String cep, String descricao, List<String> competencias) {
        this.nome = nome
        this.email = email
        this.estado = estado
        this.cep = cep
        this.descricao = descricao
        this.competencias = competencias
    }
}