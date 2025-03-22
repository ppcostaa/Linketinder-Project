package controllers

import infra.EmpresaRepository
import infra.LocalizacaoRepository
import infra.UsuarioRepository
import model.Empresa
import model.Localizacao
import model.Usuario

class EmpresaController {
    Scanner scanner = new Scanner(System.in)

    def index() {
        List<Empresa> empresas = EmpresaRepository.listarEmpresas()
        if (empresas.isEmpty()) {
            println "Nenhuma empresa cadastrada. (•◡•) /"
        } else {
            println "✦•····· Lista de Empresas ·····•✦"
            empresas.each { empresa ->
                Usuario usuario = UsuarioRepository.listarUsuariosPorId(empresa.usuarioId)
                Localizacao localizacao = LocalizacaoRepository.listarLocalizacaoPorId(empresa.localizacaoId)
                println "ID: ${empresa.empresaId}, \n" +
                        "Nome: ${empresa.empresaNome} \n" +
                        "CNPJ: ${empresa.cnpj}, \n" +
                        "Email: ${usuario.email}, \n" +
                        "CEP: ${localizacao.cep}, \n" +
                        "País: ${localizacao.pais}, \n" +
                        "Descrição: ${usuario.descricao} \n" +
                        "✦•·····•✦•·····•✦"
            }
        }
    }

    def salvarNovaEmpresaMenu() {
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

        if (UsuarioRepository.emailExiste(email)) {
            System.out.println("Email já cadastrado. Tente outro email. (•◡•) /")
            return
        }

        def empresa = new Empresa([empresaId: '0', empresaNome: nome, cnpj: cnpj])
        EmpresaRepository.salvarEmpresa(empresa, email, senha, descricao, cep, pais)
        println "Empresa cadastrada com sucesso! （っ＾▿＾）"
    }

    def editarEmpresaMenu(Empresa empresa) {
        List<Empresa> empresas = EmpresaRepository.listarEmpresas()
        if (empresas.isEmpty()) {
            println "Nenhuma empresa cadastrada para atualizar. (•◡•) /"
        }

        println "✦•····· Lista de Empresas  ·····•✦"
        empresas.each { x ->
            Usuario usuario = UsuarioRepository.listarUsuariosPorId(empresas.usuarioId)
            Localizacao localizacao = LocalizacaoRepository.listarLocalizacaoPorId(empresas.localizacaoId)

            println "ID: ${empresas.empresaId}, \n" +
                    "Nome: ${empresas.empresaNome} \n" +
                    "CNPJ: ${empresas.cnpj} \n" +
                    "Email: ${usuario.email}, \n" +
                    "CEP: ${localizacao.cep}, \n" +
                    "País: ${localizacao.pais}, \n" +
                    "Descrição: ${usuario.descricao} \n" +
                    "✦•·····•✦•·····•✦"
        }

        print "Informe o ID da empresa que deseja atualizar: "
        int empresaIdParaEditar = scanner.nextInt()
        scanner.nextLine()

        Empresa empresaPorId = EmpresaRepository.listarEmpresaPorId(empresaIdParaEditar)
        if (empresaPorId == null) {
            println "Empresa não encontrada. (╥﹏╥)"
        }

        Usuario usuario = UsuarioRepository.listarUsuariosPorId(empresa.usuarioId)
        Localizacao localizacao = LocalizacaoRepository.listarLocalizacaoPorId(empresa.localizacaoId)

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
        }

        if (opcoesSelecionadas.contains(1)) {
            print "Digite o novo nome: "
            empresa.empresaNome = scanner.nextLine()
        }
        if (opcoesSelecionadas.contains(2)) {
            print "Digite o novo CNPJ: "
            empresa.cnpj = scanner.nextLine()
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

        boolean sucessoEmpresa = EmpresaRepository.editarEmpresa(empresa)
        boolean sucessoUsuario = UsuarioRepository.editarUsuario(usuario)
        boolean sucessoLocalizacao = LocalizacaoRepository.editarLocalizacao(localizacao, empresa.empresaId)

        if (sucessoEmpresa && sucessoUsuario && sucessoLocalizacao) {
            println "Empresa atualizada com sucesso! （っ＾▿＾）"
        } else {
            println "Erro ao atualizar empresa. (╥﹏╥)"
        }
    }

    void excluirEmpresaMenu() {
        List<Empresa> empresas = EmpresaRepository.listarEmpresas();
        if (empresas.isEmpty()) {
            println("Nenhuma empresa cadastrada para excluir. (•◡•) /");
            return;
        }

        println("✦•····· Lista de Empresas ·····•✦");
        for (Empresa empresa : empresas) {
            Usuario usuario = UsuarioRepository.listarUsuariosPorId(empresa.getUsuarioId());
            Localizacao localizacao = LocalizacaoRepository.listarLocalizacaoPorId(empresa.getLocalizacaoId());

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

        boolean sucesso = EmpresaRepository.excluirEmpresa(idEmpresaDeletar);
        if (sucesso) {
            println("Empresa deletada com sucesso! （っ＾▿＾）");
        } else {
            println("Erro ao deletar empresa. (╥﹏╥)");
        }
    }
}
