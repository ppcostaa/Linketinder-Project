import { Pessoa } from "./Pessoa.js";

export class Candidato extends Pessoa {
  cpf: string;
  idade: number;
  linkedin: string;
  constructor(
    nome: string,
    email: string,
    estado: string,
    cep: string,
    descricao: string,
    competencias: string[],
    cpf: string,
    idade: number,
    linkedin: string
  ) {
    super(nome, email, estado, cep, descricao, competencias);
    this.cpf = cpf;
    this.idade = idade;
    this.linkedin = linkedin;
  }

  toString(): string {
    return `Candidato: ${this.getNome()}, ${this.getEmail()}, ${this.getEstado()}, ${this.getCep()}, ${this.getDescricao()}, ${this.getCompetencias()}, ${
      this.cpf
    }, ${this.idade}\n--------------------------------------`;
  }
}
