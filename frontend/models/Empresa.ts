import { Pessoa } from "./Pessoa";

export class Empresa extends Pessoa {
  cnpj: string;
  pais: string;

  constructor(
    nome: string,
    email: string,
    estado: string,
    cep: string,
    descricao: string,
    competencias: string[],
    cnpj: string,
    pais: string
  ) {
    super(nome, email, estado, cep, descricao, competencias);
    this.cnpj = cnpj;
    this.pais = pais;
  }

  toString(): string {
    return `Empresa: ${this.getNome()}, ${this.getEmail()}, ${this.getEstado()}, ${this.getCep()}, ${this.getDescricao()}, ${this.getCompetencias()}, ${
      this.cnpj
    }, ${this.pais}\n--------------------------------------`;
  }
}
