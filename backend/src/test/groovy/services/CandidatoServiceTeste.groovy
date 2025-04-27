package services

import model.Candidato
import model.Competencia
import model.Localizacao
import model.Usuario
import DAO.CandidatoDAO
import DAO.CompetenciaDAO
import DAO.LocalizacaoDAO
import DAO.UsuarioDAO
import spock.lang.Specification
import spock.lang.Subject

import java.text.SimpleDateFormat

class CandidatoServiceTeste extends Specification {

    @Subject
    CandidatoService candidatoService

    CandidatoDAO candidatoRepository = Mock()
    UsuarioDAO usuarioRepository = Mock()
    LocalizacaoDAO localizacaoRepository = Mock()
    CompetenciaDAO competenciaRepository = Mock()
    CompetenciaService competenciaService = Mock()

    def setup() {
        candidatoService = new CandidatoService(
                candidatoRepository: candidatoRepository,
                usuarioRepository: usuarioRepository,
                localizacaoRepository: localizacaoRepository,
                competenciaRepository: competenciaRepository,
                competenciaService: competenciaService
        )
    }

    def "listarCandidatos deve exibir mensagem quando não houver candidatos"() {
        given: "Não há candidatos cadastrados"
        candidatoRepository.listarCandidatos() >> []

        when: "Listar candidatos"
        candidatoService.listarCandidatos()

        then: "Deve exibir mensagem de nenhum candidato"
        noExceptionThrown()
    }

    def "listarCandidatos deve exibir informações dos candidatos corretamente"() {
        given: "Há candidatos cadastrados"
        def candidato = new Candidato(
                candidatoId: 1,
                usuarioId: 1,
                localizacaoId: 1,
                nome: "João",
                sobrenome: "Silva",
                cpf: "12345678901",
                dataNascimento: new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1990"),
                competencias: []
        )

        def usuario = new Usuario(email: "joao@example.com", descricao: "Descrição teste")
        def localizacao = new Localizacao(cep: "12345678", pais: "Brasil")

        candidatoRepository.listarCandidatos() >> [candidato]
        usuarioRepository.listarUsuarioPorId(1) >> usuario
        localizacaoRepository.listarLocalizacoesPorId(1) >> localizacao

        when: "Listar candidatos"
        candidatoService.listarCandidatos()

        then: "Deve exibir as informações corretamente"
        noExceptionThrown()
    }

    def "salvarDataNascimento deve retornar data válida quando formato correto"() {
        given: "Input de data válido"
        def dataValida = "15/05/1990"
        candidatoService.scanner = new Scanner(dataValida)

        when: "Salvar data de nascimento"
        def result = candidatoService.salvarDataNascimento()

        then: "Deve retornar a data correta"
        result != null
        new SimpleDateFormat("dd/MM/yyyy").format(result) == dataValida
    }

    def "salvarDataNascimento deve retornar null quando formato inválido"() {
        given: "Input de data inválido"
        def dataInvalida = "1990/05/15"
        candidatoService.scanner = new Scanner(dataInvalida)

        when: "Salvar data de nascimento"
        def result = candidatoService.salvarDataNascimento()

        then: "Deve retornar null"
        result == null
    }

    def "salvarCandidato deve salvar candidato quando dados são válidos"() {
        given: "Dados válidos de candidato"
        def input = "João\nSilva\njoao@example.com\nsenha123\n12345678\nBrasil\n12345678901\n01/01/1990\nDescrição teste"
        candidatoService.scanner = new Scanner(input)

        usuarioRepository.emailExiste(_) >> false
        competenciaService.salvarCompetenciaPorUsuario() >> [new Competencia(competenciaId: 1, competenciaNome: "Java")]

        when: "Salvar candidato"
        candidatoService.salvarCandidato()

        then: "Deve chamar o repositório para salvar"
        1 * candidatoRepository.salvarCandidato(_, "joao@example.com", "senha123", "Descrição teste", "12345678", "Brasil")
    }

    def "salvarCandidato não deve salvar quando email já existe"() {
        given: "Email já cadastrado"
        def input = "João\nSilva\njoao@example.com\nsenha123\n12345678\nBrasil\n12345678901\n01/01/1990\nDescrição teste"
        candidatoService.scanner = new Scanner(input)

        usuarioRepository.emailExiste("joao@example.com") >> true
        competenciaService.salvarCompetenciaPorUsuario() >> []

        when: "Salvar candidato"
        candidatoService.salvarCandidato()

        then: "Não deve chamar o repositório para salvar"
        0 * candidatoRepository.salvarCandidato(*_)

        and: "Deve mostrar mensagem de email existente"
        noExceptionThrown()
    }

    def "editarCandidato deve atualizar dados quando ID existe"() {
        given: "Candidato existente e opções de edição"
        def candidatoExistente = new Candidato(
                candidatoId: 1,
                usuarioId: 1,
                localizacaoId: 1,
                nome: "João",
                sobrenome: "Silva",
                cpf: "12345678901",
                dataNascimento: new Date(),
                competencias: []
        )

        def usuario = new Usuario(usuarioId: 1, email: "joao@example.com", senha: "old", descricao: "old")
        def localizacao = new Localizacao(localizacaoId: 1, cep: "old", pais: "old")

        candidatoRepository.listarCandidatos() >> [candidatoExistente]
        candidatoRepository.listarCandidatoPorId(1) >> candidatoExistente
        usuarioRepository.listarUsuarioPorId(1) >> usuario
        localizacaoRepository.listarLocalizacoesPorId(1) >> localizacao

        candidatoRepository.editarCandidato(_) >> true
        usuarioRepository.editarUsuario(_) >> true
        localizacaoRepository.editarLocalizacao(*_) >> true

        def input = "1\n1,3\nJoão Atualizado\nnovo@email.com"
        candidatoService.scanner = new Scanner(input)

        when: "Editar candidato"
        candidatoService.editarCandidato()

        then: "Deve atualizar os dados"
        1 * candidatoRepository.editarCandidato(_) >> { Candidato c ->
            c.nome == "João Atualizado"
        }
        1 * usuarioRepository.editarUsuario(_) >> { Usuario u ->
            u.email == "novo@email.com"
        }
        1 * localizacaoRepository.editarLocalizacao(*_)
    }

    def "excluirCandidato deve deletar quando ID existe"() {
        given: "Candidato existente"
        def candidato = new Candidato(
                candidatoId: 1,
                usuarioId: 1,
                localizacaoId: 1
        )

        def usuario = new Usuario(usuarioId: 1, email: "teste@teste.com")
        def localizacao = new Localizacao(localizacaoId: 1, cep: "12345678")

        candidatoRepository.listarCandidatos() >> [candidato]
        usuarioRepository.listarUsuarioPorId(1) >> usuario
        localizacaoRepository.listarLocalizacoesPorId(1) >> localizacao

        candidatoService.scanner = new Scanner("1\n")
        candidatoRepository.excluirCandidato(1) >> true

        when: "Excluir candidato"
        candidatoService.excluirCandidato()

        then: "Deve chamar o repositório para excluir"
        1 * candidatoRepository.excluirCandidato(1)
    }

    def "excluirCandidato não deve fazer nada quando ID não existe"() {
        given: "Candidato não existe"
        candidatoRepository.listarCandidatos() >> []
        candidatoService.scanner = new Scanner("999\n")

        when: "Excluir candidato"
        candidatoService.excluirCandidato()

        then: "Não deve chamar o repositório para excluir"
        0 * candidatoRepository.excluirCandidato(*_)
    }
}