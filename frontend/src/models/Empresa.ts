import { Pessoa } from "./Pessoa";

export class Empresa extends Pessoa {
  constructor(
    nome: string,
    email: string,
    estado: string,
    cep: string,
    descricao: string,
    competencias: string[],
    public readonly cnpj: string,
    public readonly pais: string
  ) {
    super(nome, email, estado, cep, descricao, competencias);
  }

  toString(): string {
    return `Empresa: ${this.getNome()}, ${this.getEmail()}, ${this.getEstado()}, ${this.getCep()}, ${this.getDescricao()}, ${this.getCompetencias()}, ${
      this.cnpj
    }, ${this.pais}\n--------------------------------------`;
  }
}
