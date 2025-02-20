package model;

class Candidato extends Pessoa {
    String cpf
    int idade

    Candidato(String nome, String email, String estado, String cep, String descricao, List<String> competencias, String cpf, int idade) {
        super(nome, email, estado, cep, descricao, competencias);        this.cpf = cpf
        this.idade = idade
    }
    @Override
    String toString() {
        return """Candidato: $nome
Email: $email
Estado: $estado
CEP: $cep
Descrição: $descricao
Competências: ${competencias.join(", ")}
Idade: $idade
--------------------------------------"""
    }
}
