import { Pessoa } from "./Pessoa.js";

export class Candidato extends Pessoa {
  cpf: string;
  idade: number;
  constructor(
    nome: string,
    email: string,
    estado: string,
    cep: string,
    descricao: string,
    competencias: string[],
    cpf: string,
    idade: number
  ) {
    super(nome, email, estado, cep, descricao, competencias);
    this.cpf = cpf;
    this.idade = idade;
  }

  toString(): string {
    return `Candidato: ${this.getNome()}, ${this.getEmail()}, ${this.getEstado()}, ${this.getCep()}, ${this.getDescricao()}, ${this.getCompetencias()}, ${
      this.cpf
    }, ${this.idade}\n--------------------------------------`;
  }
}
