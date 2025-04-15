package groovy.services

import model.Competencia
import repository.CompetenciaRepository
import services.CompetenciaService
import spock.lang.Specification
import spock.lang.Subject

class CompetenciaServiceTeste extends Specification {

    @Subject
    CompetenciaService service

    CompetenciaRepository repository = Mock()
    def scanner = GroovyMock(Scanner)

    def setup() {
        service = new CompetenciaService()
        service.competenciaRepository = repository
        service.scanner = scanner
    }

    def "deve listar competências corretamente quando existem"() {
        given: "há competências no repositório"
        def competencias = [
                new Competencia(competenciaId: 1, competenciaNome: "Java"),
                new Competencia(competenciaId: 2, competenciaNome: "Groovy")
        ]
        repository.listarCompetencias() >> competencias

        when: "lista as competências"
        service.listarCompetencias()

        then: "não lança exceção"
        noExceptionThrown()
    }

    def "deve listar mensagem quando não há competências"() {
        given: "não há competências no repositório"
        repository.listarCompetencias() >> []

        when: "lista as competências"
        service.listarCompetencias()

        then: "não lança exceção"
        noExceptionThrown()
    }

    def "deve salvar competência com sucesso"() {
        given: "dados válidos para nova competência"
        scanner.nextLine() >> "Java"

        and: "repositório mockado"
        def competenciaSalva = new Competencia(competenciaId: 1, competenciaNome: "Java")
        repository.salvarCompetencia(_ as Competencia) >> competenciaSalva

        when: "salva a competência"
        service.salvarCompetencia()

        then: "não lança exceção"
        noExceptionThrown()
    }

    def "deve editar competência existente com sucesso"() {
        given: "dados válidos para edição"
        scanner.nextInt() >> 1
        scanner.nextLine() >>> ["", "Java Atualizado"]

        and: "competência existente e lista não vazia"
        def competenciaExistente = new Competencia(competenciaId: 1, competenciaNome: "Java")
        repository.listarCompetenciasPorId(1) >> competenciaExistente
        repository.listarCompetencias() >> [competenciaExistente]
        repository.editarCompetencia(_ as Competencia) >> true

        when: "edita a competência"
        service.editarCompetencia()

        then: "não lança exceção"
        noExceptionThrown()
    }

    def "deve mostrar mensagem ao tentar editar competência inexistente"() {
        given: "ID de competência inexistente"
        scanner.nextInt() >> 999
        scanner.nextLine() >> ""
        repository.listarCompetenciasPorId(999) >> null
        repository.listarCompetencias() >> []

        when: "tenta editar competência"
        service.editarCompetencia()

        then: "não lança exceção"
        noExceptionThrown()
    }

    def "deve excluir competência existente com sucesso"() {
        given: "ID de competência existente"
        scanner.nextInt() >> 1
        scanner.nextLine() >> ""

        and: "lista não vazia"
        def competenciaExistente = new Competencia(competenciaId: 1, competenciaNome: "Java")
        repository.listarCompetencias() >> [competenciaExistente]
        repository.excluirCompetencia(1) >> true

        when: "exclui a competência"
        service.excluirCompetencia()

        then: "não lança exceção"
        noExceptionThrown()
    }

    def "deve mostrar mensagem ao tentar excluir competência inexistente"() {
        given: "ID de competência inexistente"
        scanner.nextInt() >> 999
        scanner.nextLine() >> ""
        repository.excluirCompetencia(999) >> false
        repository.listarCompetencias() >> []

        when: "tenta excluir competência"
        service.excluirCompetencia()

        then: "não lança exceção"
        noExceptionThrown()
    }


    def "deve selecionar competências por usuário corretamente"() {
        given: "competências disponíveis"
        def competenciasDisponiveis = [
                new Competencia(competenciaId: 1, competenciaNome: "Java"),
                new Competencia(competenciaId: 2, competenciaNome: "Groovy")
        ]
        repository.listarCompetencias() >> competenciasDisponiveis

        and: "input do usuário"
        scanner.nextLine() >> "1,2"

        when: "seleciona competências"
        def resultado = service.salvarCompetenciaPorUsuario()

        then: "retorna competências selecionadas"
        resultado.size() == 2
        resultado[0].competenciaId == 1
        resultado[1].competenciaId == 2
    }
}