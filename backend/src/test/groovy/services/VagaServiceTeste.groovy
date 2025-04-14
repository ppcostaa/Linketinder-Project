package services

import groovy.infra.CompetenciaRepository
import groovy.infra.EmpresaRepository
import groovy.infra.VagaRepository
import groovy.model.Competencia
import groovy.model.Empresa
import groovy.model.Vaga
import groovy.services.CompetenciaService
import groovy.services.VagaService
import spock.lang.Specification
import spock.lang.Subject

class VagaServiceTeste extends Specification {

    @Subject
    VagaService vagaService

    VagaRepository vagaRepository = Mock()
    CompetenciaRepository competenciaRepository = Mock()
    CompetenciaService competenciaService = Mock()
    EmpresaRepository empresaRepository = Mock()

    def setup() {
        vagaService = new VagaService(
                vagaRepository: vagaRepository,
                competenciaRepository: competenciaRepository,
                competenciaService: competenciaService,
                empresaRepository: empresaRepository
        )
    }

    def "listarVagas deve exibir mensagem quando não houver vagas"() {
        given: "Não há vagas cadastradas"
        vagaRepository.listarVagas() >> []

        when: "Listar vagas"
        vagaService.listarVagas()

        then: "Deve exibir mensagem de nenhuma vaga"
        noExceptionThrown()
    }

    def "listarVagas deve exibir informações das vagas corretamente"() {
        given: "Há vagas cadastradas"
        def vaga = new Vaga(
                vagaId: 1,
                empresaId: 1,
                titulo: "Desenvolvedor Java",
                descricao: "Vaga para desenvolvedor Java",
                estado: "SP",
                cidade: "São Paulo",
                competencias: [new Competencia(competenciaId: 1, competenciaNome: "Java")]
        )

        def empresa = new Empresa(empresaId: 1, empresaNome: "Empresa Teste")

        vagaRepository.listarVagas() >> [vaga]
        empresaRepository.listarEmpresaPorId(1) >> empresa

        when: "Listar vagas"
        vagaService.listarVagas()

        then: "Deve exibir as informações corretamente"
        noExceptionThrown()
    }

    def "salvarVaga deve salvar quando empresa existe e dados são válidos"() {
        given: "Empresa existente e dados válidos"
        def empresa = new Empresa(empresaId: 1, empresaNome: "Empresa Teste")
        def vagaSalva = new Vaga(
                vagaId: 1,
                empresaId: 1,
                titulo: "Desenvolvedor Java",
                descricao: "Descrição da vaga",
                estado: "SP",
                cidade: "São Paulo",
                competencias: [new Competencia(competenciaId: 1)]
        )

        empresaRepository.listarEmpresas() >> [empresa]
        competenciaService.salvarCompetenciaPorUsuario() >> [new Competencia(competenciaId: 1)]
        vagaRepository.salvarVaga(_) >> vagaSalva

        def input = "1\nDesenvolvedor Java\nDescrição da vaga\nSP\nSão Paulo"
        vagaService.scanner = new Scanner(input)

        when: "Salvar vaga"
        vagaService.salvarVaga()

        then: "Deve chamar o repositório para salvar"
        1 * vagaRepository.salvarVaga(_ as Vaga) >> { Vaga v ->
            v.titulo == "Desenvolvedor Java" &&
                    v.empresaId == 1
            vagaSalva
        }
    }

    def "salvarVaga não deve salvar quando não há empresas cadastradas"() {
        given: "Não há empresas cadastradas"
        empresaRepository.listarEmpresas() >> []

        when: "Salvar vaga"
        vagaService.salvarVaga()

        then: "Não deve chamar o repositório para salvar"
        0 * vagaRepository.salvarVaga(*_)

        and: "Deve mostrar mensagem de nenhuma empresa"
        noExceptionThrown()
    }

    def "editarVaga deve atualizar dados quando ID existe"() {
        given: "Vaga existente e opções de edição"
        def vagaExistente = new Vaga(
                vagaId: 1,
                empresaId: 1,
                titulo: "Desenvolvedor Java",
                descricao: "Vaga para desenvolvedor Java",
                estado: "SP",
                cidade: "São Paulo",
                competencias: [new Competencia(competenciaId: 1)]
        )

        vagaRepository.listarVagas() >> [vagaExistente]
        vagaRepository.listarVagaPorId(1) >> vagaExistente
        vagaRepository.editarVaga(_) >> true

        def competenciasDisponiveis = [new Competencia(competenciaId: 1, competenciaNome: "Java")]
        competenciaRepository.listarCompetencias() >> competenciasDisponiveis

        def input = "1\n1,2,5\nNovo Título\nSP\nNova Descrição"
        vagaService.scanner = new Scanner(input)

        when: "Editar vaga"
        vagaService.editarVaga()

        then: "Deve atualizar os dados"
        1 * vagaRepository.editarVaga(_) >> { Vaga v ->
            v.titulo == "Novo Título" &&
                    v.estado == "SP" &&
                    v.descricao == "Nova Descrição"
        }
    }

    def "excluirVaga deve deletar quando ID existe"() {
        given: "Configuração dos mocks"
        def vagaMock = new Vaga(vagaId: 1, titulo: "Vaga Teste")

        vagaRepository.listarVagas() >> [vagaMock]

        vagaRepository.listarVagaPorId(1) >> vagaMock

        vagaRepository.excluirVaga(1) >> true

        def input = new ByteArrayInputStream("1\n".getBytes())
        System.setIn(input)
        vagaService.scanner = new Scanner(System.in)

        when: "Chamando o método de exclusão"
        def resultado = vagaService.excluirVaga()

        then: "Verifica os comportamentos esperados"
        1 * vagaRepository.listarVagas() >> [vagaMock]
        1 * vagaRepository.listarVagaPorId(1) >> vagaMock
        1 * vagaRepository.excluirVaga(1) >> true
        resultado == true

        cleanup:
        System.setIn(System.in)
    }
    def "excluirVaga não deve fazer nada quando ID não existe"() {
        given: "Vaga não existe"
        def vagas = []  // Empty list for first call to listarVagas
        vagaRepository.listarVagas() >> vagas  // This ensures a non-null empty list is returned
        vagaRepository.listarVagaPorId(999) >> null

        def input = "999\n"
        vagaService.scanner = new Scanner(input)

        when: "Excluir vaga"
        vagaService.excluirVaga()

        then: "Não deve chamar o repositório para excluir"
        0 * vagaRepository.excluirVaga(_)

        and: "Deve verificar se a vaga existe"
        1 * vagaRepository.listarVagaPorId(999)
    }
}