"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.Empresa = void 0;
const Pessoa_1 = require("./Pessoa");
class Empresa extends Pessoa_1.Pessoa {
    constructor(nome, email, estado, cep, descricao, competencias, cnpj, pais) {
        super(nome, email, estado, cep, descricao, competencias);
        this.cnpj = cnpj;
        this.pais = pais;
    }
    toString() {
        return `Empresa: ${this.getNome()}, ${this.getEmail()}, ${this.getEstado()}, ${this.getCep()}, ${this.getDescricao()}, ${this.getCompetencias()}, ${this.cnpj}, ${this.pais}\n--------------------------------------`;
    }
}
exports.Empresa = Empresa;
