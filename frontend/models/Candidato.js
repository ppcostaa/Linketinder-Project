import { Pessoa } from "./Pessoa.js";
export class Candidato extends Pessoa {
    constructor(nome, email, estado, cep, descricao, competencias, cpf, idade) {
        super(nome, email, estado, cep, descricao, competencias);
        this.cpf = cpf;
        this.idade = idade;
    }
    toString() {
        return `Candidato: ${this.getNome()}, ${this.getEmail()}, ${this.getEstado()}, ${this.getCep()}, ${this.getDescricao()}, ${this.getCompetencias()}, ${this.cpf}, ${this.idade}\n--------------------------------------`;
    }
}
