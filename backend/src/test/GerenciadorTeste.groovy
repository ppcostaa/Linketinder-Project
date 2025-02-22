package test

import model.Candidato
import model.Empresa
import spock.lang.Specification
import utils.Gerenciador

class GerenciadorTeste extends Specification{
    def CadastrarNovoCandidatoCorretamente() {
        given: "Um gerenciador e um candidato válido"
        def gerenciador = new Gerenciador()
        def candidato = new Candidato("Gustavo Lima", "gustavolima@email.com", "MG", "12345-678", "Cantor, aprendendo programação", ["Python"], "987.654.321-00", 35)

        when: "Candidato cadastrado"
        gerenciador.cadastrarCandidato(candidato)

        then: "A lista de candidatos contém o novo candidato"
        assert gerenciador.listarCandidatos().size() == 1
        assert gerenciador.listarCandidatos().contains(candidato)
    }

    def CadastrarNovaEmpresaCorretamente() {
        given: "Um gerenciador e uma empresa válida"
        def gerenciador = new Gerenciador()
        def empresa = new Empresa("InovaTech", "contato@inovatech.com", "SP", "54321-876", "Startup de Java", ["Java"], "98.765.432/0001-10", "Brasil")

        when: "A empresa é cadastrada"
        gerenciador.cadastrarEmpresa(empresa)

        then: "A lista de empresas contém a nova empresa"
        assert gerenciador.listarEmpresas().size() == 1
        assert gerenciador.listarEmpresas().contains(empresa)
    }

}
