"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.Candidato = void 0;
const Pessoa_1 = require("./Pessoa");
class Candidato extends Pessoa_1.Pessoa {
    constructor(nome, email, estado, cep, descricao, competencias, cpf, idade) {
        super(nome, email, estado, cep, descricao, competencias);
        this.cpf = cpf;
        this.idade = idade;
    }
    toString() {
        return `Candidato: ${this.getNome()}, ${this.getEmail()}, ${this.getEstado()}, ${this.getCep()}, ${this.getDescricao()}, ${this.getCompetencias()}, ${this.cpf}, ${this.idade}\n--------------------------------------`;
    }
}
exports.Candidato = Candidato;
