package utils

import main.Sistema;
import model.Candidato
import model.Empresa

class Gerenciador {
    List<Candidato> candidatos = []
    List<Empresa> empresas = []

    Sistema sistema

    Gerenciador(Sistema sistema) {
        this.sistema = sistema
    }
    def cadastrarCandidato(Candidato candidato) {
        candidatos.add(candidato)
    }

    def cadastrarEmpresa(Empresa empresa) {
        empresas.add(empresa)
    }

    def listarCandidatos() {
        return candidatos
    }

    def listarEmpresas() {
        return empresas
    }
}
