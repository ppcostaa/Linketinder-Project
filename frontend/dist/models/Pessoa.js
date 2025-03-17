export class Pessoa {
    nome;
    email;
    estado;
    cep;
    descricao;
    competencias;
    constructor(nome, email, estado, cep, descricao, competencias) {
        this.nome = nome;
        this.email = email;
        this.estado = estado;
        this.cep = cep;
        this.descricao = descricao;
        this.competencias = competencias;
    }
    getNome() {
        return this.nome;
    }
    getEmail() {
        return this.email;
    }
    getEstado() {
        return this.estado;
    }
    getCep() {
        return this.cep;
    }
    getDescricao() {
        return this.descricao;
    }
    getCompetencias() {
        return this.competencias;
    }
}
