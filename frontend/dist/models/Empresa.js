import { Pessoa } from "./Pessoa";
export class Empresa extends Pessoa {
    cnpj;
    pais;
    constructor(nome, email, estado, cep, descricao, competencias, cnpj, pais) {
        super(nome, email, estado, cep, descricao, competencias);
        this.cnpj = cnpj;
        this.pais = pais;
    }
    toString() {
        return `Empresa: ${this.getNome()}, ${this.getEmail()}, ${this.getEstado()}, ${this.getCep()}, ${this.getDescricao()}, ${this.getCompetencias()}, ${this.cnpj}, ${this.pais}\n--------------------------------------`;
    }
}
