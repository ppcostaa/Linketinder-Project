package groovy.services


import groovy.infra.EmpresaRepository
import groovy.infra.LocalizacaoRepository
import groovy.infra.UsuarioRepository
import groovy.model.Empresa
import groovy.model.Localizacao
import groovy.model.Usuario

class EmpresaService {
    Scanner scanner = new Scanner(System.in)
    EmpresaRepository empresaRepository = new EmpresaRepository()
    LocalizacaoRepository localizacaoRepository = new LocalizacaoRepository()
    UsuarioRepository usuarioRepository = new UsuarioRepository()

    def listarEmpresas() {
        List<Empresa> empresas = empresaRepository.listarEmpresas()
        if (empresas.isEmpty()) {
            println "Nenhuma empresa cadastrada. (•◡•) /"
        } else {
            println "✦•····· Lista de Empresas ·····•✦"
            empresas.each { empresa ->
                Usuario usuario = usuarioRepository.listarUsuarioPorId(empresa.usuarioId)
                Localizacao localizacao = localizacaoRepository.listarLocalizacoesPorId(empresa.localizacaoId)

                if (usuario == null || localizacao == null) {
                    println "Dados incompletos para empresa ID: ${empresa.empresaId}"
                    return
                }

                println "ID: ${empresa.empresaId}, \n" +
                        "CNPJ: ${empresa.cnpj}, \n" +
                        "Email: ${usuario.email}, \n" +
                        "CEP: ${localizacao.cep}, \n" +
                        "País: ${localizacao.pais}, \n" +
                        "Descrição: ${usuario.descricao} \n" +
                        "✦•·····•✦•·····•✦"
            }
        }
    }
    def salvarEmpresa() {
        println "Informe os dados da Empresa:"
        print "Nome: "
        String nome = scanner.nextLine()
        print "CNPJ: "
        String cnpj = scanner.nextLine()
        print "Email: "
        String email = scanner.nextLine()
        print "Senha: "
        String senha = scanner.nextLine()
        print "CEP: "
        String cep = scanner.nextLine()
        print "País: "
        String pais = scanner.nextLine()
        print "Descrição: "
        String descricao = scanner.nextLine()

        if (usuarioRepository.emailExiste(email)) {
            println "Email já cadastrado. Tente outro email. (•◡•) /"
            return
        }

        Empresa empresa = new Empresa()
        empresa.empresaNome = nome
        empresa.cnpj = cnpj

        empresaRepository.salvarEmpresa(empresa, email, senha, descricao, cep, pais)
        println "Empresa cadastrada com sucesso! （っ＾▿＾）"
    }

    def editarEmpresa() {
        listarEmpresas()

        print "Informe o ID da empresa que deseja atualizar: "
        int empresaIdParaEditar = scanner.nextInt()
        scanner.nextLine()

        Empresa empresaPorId = empresaRepository.listarEmpresaPorId(empresaIdParaEditar)
        if (empresaPorId == null) {
            println "Empresa não encontrada. (╥﹏╥)"
            return
        }

        Usuario usuario = usuarioRepository.listarUsuarioPorId(empresaPorId.usuarioId)
        Localizacao localizacao = localizacaoRepository.listarLocalizacoesPorId(empresaPorId.localizacaoId)

        println "\nO que você deseja atualizar?"
        println "1. Nome"
        println "2. CNPJ"
        println "3. Email"
        println "4. Senha"
        println "5. CEP"
        println "6. País"
        println "7. Descrição"
        print "(Digite os números separados por vírgula): "
        String opcoes = scanner.nextLine()

        List<Integer> opcoesSelecionadas = opcoes.split(',').collect { it.trim().toInteger() }
        List<Integer> opcoesValidas = [1, 2, 3, 4, 5, 6, 7]

        if (opcoesSelecionadas.any { !opcoesValidas.contains(it) }) {
            println "Opção inválida! Escolha números entre 1 e 7. (╥﹏╥)"
            return
        }

        if (opcoesSelecionadas.contains(1)) {
            print "Digite o novo nome: "
            empresaPorId.empresaNome = scanner.nextLine()
        }
        if (opcoesSelecionadas.contains(2)) {
            print "Digite o novo CNPJ: "
            empresaPorId.cnpj = scanner.nextLine()
        }
        if (opcoesSelecionadas.contains(3)) {
            print "Digite o novo email: "
            usuario.email = scanner.nextLine()
        }
        if (opcoesSelecionadas.contains(4)) {
            print "Digite a nova Senha: "
            usuario.senha = scanner.nextLine()
        }
        if (opcoesSelecionadas.contains(5)) {
            print "Digite o novo CEP: "
            localizacao.cep = scanner.nextLine()
        }
        if (opcoesSelecionadas.contains(6)) {
            print "Digite o novo País: "
            localizacao.pais = scanner.nextLine()
        }
        if (opcoesSelecionadas.contains(7)) {
            print "Digite a nova Descrição: "
            usuario.descricao = scanner.nextLine()
        }

        boolean sucessoEmpresa = empresaRepository.editarEmpresa(empresaPorId)
        boolean sucessoUsuario = usuarioRepository.editarUsuario(usuario)
        boolean sucessoLocalizacao = localizacaoRepository.editarLocalizacao(localizacao, empresaPorId.empresaId)

        if (sucessoEmpresa && sucessoUsuario && sucessoLocalizacao) {
            println "Empresa atualizada com sucesso! （っ＾▿＾）"
        } else {
            println "Erro ao atualizar empresa. (╥﹏╥)"
        }
    }

    def excluirEmpresa() {
        listarEmpresas()

        print("Informe o ID da Empresa que deseja deletar: ");
        int idEmpresaDeletar = scanner.nextInt();
        scanner.nextLine();

        boolean sucesso = empresaRepository.excluirEmpresa(idEmpresaDeletar);
        if (sucesso) {
            println("Empresa deletada com sucesso! （っ＾▿＾）");
        } else {
            println("Erro ao deletar empresa. (╥﹏╥)");
        }
    }
}
