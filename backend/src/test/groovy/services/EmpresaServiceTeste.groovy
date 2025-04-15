package services

import model.Empresa
import model.Localizacao
import model.Usuario
import repository.EmpresaRepository
import repository.LocalizacaoRepository
import repository.UsuarioRepository
import spock.lang.Specification

class EmpresaServiceTeste extends Specification {

    EmpresaService empresaService
    EmpresaRepository mockEmpresaRepository
    UsuarioRepository mockUsuarioRepository
    LocalizacaoRepository mockLocalizacaoRepository
    InputStream originalIn
    PrintStream originalOut
    ByteArrayOutputStream outputStream

    def setup() {
        originalIn = System.in
        originalOut = System.out

        outputStream = new ByteArrayOutputStream()
        System.setOut(new PrintStream(outputStream))

        mockEmpresaRepository = Mock(EmpresaRepository)
        mockUsuarioRepository = Mock(UsuarioRepository)
        mockLocalizacaoRepository = Mock(LocalizacaoRepository)

        empresaService = new EmpresaService(
                empresaRepository: mockEmpresaRepository,
                usuarioRepository: mockUsuarioRepository,
                localizacaoRepository: mockLocalizacaoRepository
        )
    }

    def cleanup() {
        System.setIn(originalIn)
        System.setOut(originalOut)
    }

    def "deve listar empresas quando existirem empresas cadastradas"() {
        given: "uma lista de empresas"
        def empresas = [
                new Empresa(empresaId: 1, usuarioId: 10, localizacaoId: 100, empresaNome: "Empresa A", cnpj: "11111111111111"),
                new Empresa(empresaId: 2, usuarioId: 20, localizacaoId: 200, empresaNome: "Empresa B", cnpj: "22222222222222")
        ]

        and: "usuários e localizações associados"
        def usuarios = [
                new Usuario(usuarioId: 10, email: "empresaA@teste.com", descricao: "Descrição A"),
                new Usuario(usuarioId: 20, email: "empresaB@teste.com", descricao: "Descrição B")
        ]

        def localizacoes = [
                new Localizacao(cep: "11111111", pais: "Brasil"),
                new Localizacao(cep: "22222222", pais: "Portugal")
        ]

        and: "configuração dos mocks"
        mockEmpresaRepository.listarEmpresas() >> empresas
        mockUsuarioRepository.listarUsuarioPorId(10) >> usuarios[0]
        mockUsuarioRepository.listarUsuarioPorId(20) >> usuarios[1]
        mockLocalizacaoRepository.listarLocalizacoesPorId(100) >> localizacoes[0]
        mockLocalizacaoRepository.listarLocalizacoesPorId(200) >> localizacoes[1]

        when: "o método listarEmpresas é chamado"
        empresaService.listarEmpresas()

        then: "deve exibir as informações das empresas"
        outputStream.toString().contains("Lista de Empresas")
        outputStream.toString().contains("ID: 1")
        outputStream.toString().contains("CNPJ: 11111111111111")
        outputStream.toString().contains("Email: empresaA@teste.com")
        outputStream.toString().contains("País: Brasil")
    }

    def "deve informar quando não existem empresas cadastradas"() {
        given: "lista de empresas vazia"
        mockEmpresaRepository.listarEmpresas() >> []

        when: "o método listarEmpresas é chamado"
        empresaService.listarEmpresas()

        then: "deve informar que não há empresas cadastradas"
        outputStream.toString().contains("Nenhuma empresa cadastrada")
    }

    def "deve salvar empresa com sucesso"() {
        given: "mock do scanner para simular entrada do usuário"
        def input = "Empresa Teste\n12345678901234\nteste@empresa.com\nsenha123\n12345678\nBrasil\nDescrição da empresa\n"
        System.setIn(new ByteArrayInputStream(input.getBytes()))
        empresaService.scanner = new Scanner(System.in)

        and: "configuração dos mocks para verificação e salvamento"
        mockUsuarioRepository.emailExiste("teste@empresa.com") >> false

        when: "o método salvarEmpresa é chamado"
        empresaService.salvarEmpresa()

        then: "deve chamar o repositório para salvar a empresa"
        1 * mockEmpresaRepository.salvarEmpresa({ it.empresaNome == "Empresa Teste" && it.cnpj == "12345678901234" },
                "teste@empresa.com", "senha123", "Descrição da empresa", "12345678", "Brasil")

        and: "deve exibir mensagem de sucesso"
        outputStream.toString().contains("Empresa cadastrada com sucesso")
    }

    def "não deve salvar empresa quando email já existe"() {
        given: "mock do scanner para simular entrada do usuário"
        def input = "Empresa Teste\n12345678901234\nteste@empresa.com\nsenha123\n12345678\nBrasil\nDescrição da empresa\n"
        System.setIn(new ByteArrayInputStream(input.getBytes()))
        empresaService.scanner = new Scanner(System.in)

        and: "configuração para email já existente"
        mockUsuarioRepository.emailExiste("teste@empresa.com") >> true

        when: "o método salvarEmpresa é chamado"
        empresaService.salvarEmpresa()

        then: "não deve chamar o repositório para salvar"
        0 * mockEmpresaRepository.salvarEmpresa(_, _, _, _, _, _)

        and: "deve exibir mensagem de erro"
        outputStream.toString().contains("Email já cadastrado")
    }

    def "deve editar empresa com sucesso"() {
        given: "mock do scanner para simular entrada do usuário"
        def input = "1\n1,2\nEmpresa Atualizada\n98765432109876\n"
        System.setIn(new ByteArrayInputStream(input.getBytes()))
        empresaService.scanner = new Scanner(System.in)

        and: "empresa existente para edição"
        def empresa = new Empresa(
                empresaId: 1,
                usuarioId: 10,
                localizacaoId: 100,
                empresaNome: "Empresa Original",
                cnpj: "12345678901234"
        )

        def usuario = new Usuario(usuarioId: 10, email: "empresa@teste.com", senha: "senha123", descricao: "Descrição")
        def localizacao = new Localizacao(cep: "12345678", pais: "Brasil")
        and: "configuração dos mocks"
        mockEmpresaRepository.listarEmpresas() >> [empresa]
        mockEmpresaRepository.listarEmpresaPorId(1) >> empresa
        mockUsuarioRepository.listarUsuarioPorId(10) >> usuario
        mockLocalizacaoRepository.listarLocalizacoesPorId(100) >> localizacao
        mockEmpresaRepository.editarEmpresa(_) >> true
        mockUsuarioRepository.editarUsuario(_) >> true
        mockLocalizacaoRepository.editarLocalizacao(_, _) >> true

        when: "o método editarEmpresa é chamado"
        empresaService.editarEmpresa()

        then: "deve chamar os repositórios para editar"
        1 * mockEmpresaRepository.editarEmpresa({
            it.empresaNome == "Empresa Atualizada" &&
                    it.cnpj == "98765432109876"
        }) >> true

        and: "deve exibir mensagem de sucesso"
        outputStream.toString().contains("Empresa atualizada com sucesso")
    }

    def "deve informar quando empresa a editar não foi encontrada"() {
        given: "mock do scanner para simular entrada do usuário"
        def input = "999\n"
        System.setIn(new ByteArrayInputStream(input.getBytes()))
        empresaService.scanner = new Scanner(System.in)

        and: "configuração para empresa não encontrada"
        mockEmpresaRepository.listarEmpresas() >> [new Empresa(empresaId: 1)]
        mockEmpresaRepository.listarEmpresaPorId(999) >> null

        when: "o método editarEmpresa é chamado"
        empresaService.editarEmpresa()

        then: "não deve chamar os repositórios para editar"
        0 * mockEmpresaRepository.editarEmpresa(_)
        0 * mockUsuarioRepository.editarUsuario(_)
        0 * mockLocalizacaoRepository.editarLocalizacao(_, _)

        and: "deve exibir mensagem de erro"
        outputStream.toString().contains("Empresa não encontrada")
    }

    def "deve excluir empresa com sucesso"() {
        given: "mock do scanner para simular entrada do usuário"
        def input = "1\n"
        System.setIn(new ByteArrayInputStream(input.getBytes()))
        empresaService.scanner = new Scanner(System.in)

        and: "empresa existente para exclusão"
        def empresa = new Empresa(empresaId: 1, usuarioId: 10, localizacaoId: 100)

        and: "configuração dos mocks"
        mockEmpresaRepository.listarEmpresas() >> [
                new Empresa(empresaId: 1, usuarioId: 1, localizacaoId: 1, empresaNome: "Test", cnpj: "123")
        ]
        mockEmpresaRepository.excluirEmpresa(1) >> true

        when: "o método excluirEmpresa é chamado"
        empresaService.excluirEmpresa()

        then: "deve chamar o repositório para excluir"
        1 * mockEmpresaRepository.excluirEmpresa(1) >> true

        and: "deve exibir mensagem de sucesso"
        outputStream.toString().contains("Empresa deletada com sucesso")
    }

    def "deve informar quando houver erro ao excluir empresa"() {
        given: "mock do scanner para simular entrada do usuário"
        def input = "1\n"
        System.setIn(new ByteArrayInputStream(input.getBytes()))
        empresaService.scanner = new Scanner(System.in)

        and: "empresa existente para exclusão"
        def empresa = new Empresa(empresaId: 1)

        and: "configuração dos mocks para falha"
        mockEmpresaRepository.listarEmpresas() >> [
                new Empresa(empresaId: 1, usuarioId: 1, localizacaoId: 1, empresaNome: "Test", cnpj: "123")
        ]
        mockEmpresaRepository.excluirEmpresa(1) >> false

        when: "o método excluirEmpresa é chamado"
        empresaService.excluirEmpresa()

        then: "deve chamar o repositório para excluir"
        1 * mockEmpresaRepository.excluirEmpresa(1) >> false

        and: "deve exibir mensagem de erro"
        outputStream.toString().contains("Erro ao deletar empresa")
    }
}