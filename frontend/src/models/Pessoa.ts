import { IPessoa } from "../interfaces/IPessoa";

export abstract class Pessoa implements IPessoa {
  nome: string;
  email: string;
  estado: string;
  cep: string;
  descricao: string;
  competencias: string[];

  constructor(
    nome: string,
    email: string,
    estado: string,
    cep: string,
    descricao: string,
    competencias: string[]
  ) {
    this.nome = nome;
    this.email = email;
    this.estado = estado;
    this.cep = cep;
    this.descricao = descricao;
    this.competencias = competencias;
  }

  getNome(): string {
    return this.nome;
  }
  getEmail(): string {
    return this.email;
  }
  getEstado(): string {
    return this.estado;
  }
  getCep(): string {
    return this.cep;
  }
  getDescricao(): string {
    return this.descricao;
  }
  getCompetencias(): string[] {
    return this.competencias;
  }

  abstract toString(): string;
}
