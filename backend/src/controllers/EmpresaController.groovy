package controllers


import infra.EmpresaRepository
import infra.LocalizacaoRepository
import infra.UsuarioRepository
import model.Empresa
import model.Localizacao
import model.Usuario

class EmpresaController {
    Scanner scanner = new Scanner(System.in)
    EmpresaRepository empresaRepository = new EmpresaRepository()
    LocalizacaoRepository localizacaoRepository = new LocalizacaoRepository()
    UsuarioRepository usuarioRepository = new UsuarioRepository()

    def index() {
        List<Empresa> empresas = empresaRepository.listarEmpresas()
        if (empresas.isEmpty()) {
            println "Nenhuma empresa cadastrada. (•◡•) /"
        } else {
            println "✦•····· Lista de Empresas ·····•✦"
            empresas.each { empresa ->
                Usuario usuario = usuarioRepository.listarUsuariosPorId(empresa.usuarioId)
                Localizacao localizacao = localizacaoRepository.listarLocalizacaoPorId(empresa.localizacaoId)
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

    def salvarEmpresaMenu() {
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


    def editarEmpresaMenu() {
        List<Empresa> empresas = empresaRepository.listarEmpresas()
        if (empresas.isEmpty()) {
            println "Nenhuma empresa cadastrada para atualizar. (•◡•) /"
            return
        }

        println "✦•····· Lista de Empresas  ·····•✦"
        empresas.each { empresasLista ->
            Usuario usuario = usuarioRepository.listarUsuariosPorId(empresasLista.usuarioId)
            Localizacao localizacao = localizacaoRepository.listarLocalizacaoPorId(empresasLista.localizacaoId)

            println "ID: ${empresasLista.empresaId}, \n" +
                    "Nome: ${empresasLista.empresaNome} \n" +
                    "CNPJ: ${empresasLista.cnpj} \n" +
                    "Email: ${usuario.email}, \n" +
                    "CEP: ${localizacao.cep}, \n" +
                    "País: ${localizacao.pais}, \n" +
                    "Descrição: ${usuario.descricao} \n" +
                    "✦•·····•✦•·····•✦"
        }

        print "Informe o ID da empresa que deseja atualizar: "
        int empresaIdParaEditar = scanner.nextInt()
        scanner.nextLine()

        Empresa empresaPorId = empresaRepository.listarEmpresaPorId(empresaIdParaEditar)
        if (empresaPorId == null) {
            println "Empresa não encontrada. (╥﹏╥)"
            return
        }

        Usuario usuario = usuarioRepository.listarUsuariosPorId(empresaPorId.usuarioId)
        Localizacao localizacao = localizacaoRepository.listarLocalizacaoPorId(empresaPorId.localizacaoId)

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

    def excluirEmpresaMenu() {
        List<Empresa> empresas = empresaRepository.listarEmpresas();
        if (empresas.isEmpty()) {
            println("Nenhuma empresa cadastrada para excluir. (•◡•) /");
        }

        println("✦•····· Lista de Empresas ·····•✦");
        for (Empresa empresa : empresas) {
            Usuario usuario = usuarioRepository.listarUsuariosPorId(empresa.getUsuarioId());
            Localizacao localizacao = localizacaoRepository.listarLocalizacaoPorId(empresa.getLocalizacaoId());

            println("ID: " + empresa.getEmpresaId() + "\n" +
                    "Nome: " + empresa.getEmpresaNome() + "\n" +
                    "CNPJ: " + empresa.getCnpj() + "\n" +
                    "Email: " + usuario.getEmail() + "\n" +
                    "CEP: " + localizacao.getCep() + "\n" +
                    "País: " + localizacao.getPais() + "\n" +
                    "Descrição: " + usuario.getDescricao() + "\n" +
                    "✦•·····•✦•·····•✦"
            );
        }

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
