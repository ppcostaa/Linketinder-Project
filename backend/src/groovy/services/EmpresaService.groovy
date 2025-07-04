package services


import DAO.EmpresaDAO
import DAO.LocalizacaoDAO
import DAO.UsuarioDAO
import model.Empresa
import model.Localizacao
import model.Usuario

class EmpresaService {
    Scanner scanner = new Scanner(System.in)
    EmpresaDAO empresaDAO = new EmpresaDAO()
    LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO()
    UsuarioDAO usuarioDAO = new UsuarioDAO()

    List<Empresa> buscarTodasEmpresas() {
        return empresaDAO.listarEmpresas()
    }

    Empresa buscarEmpresaPorId(int id) {
        return empresaDAO.listarEmpresaPorId(id)
    }

    Empresa salvarEmpresa(Empresa empresa, String email, String senha, String descricao, String cep, String pais) {
        if (usuarioDAO.emailExiste(email)) {
            throw new RuntimeException("Email já cadastrado. Tente outro email.")
        }

        return empresaDAO.salvarEmpresa(empresa, email, senha, descricao, cep, pais)
    }

    boolean editarEmpresa(Empresa empresa) {
        return empresaDAO.editarEmpresa(empresa)
    }

    boolean excluirEmpresa(int id) {
        return empresaDAO.excluirEmpresa(id)
    }

    def listarEmpresas() {
        List<Empresa> empresas = empresaDAO.listarEmpresas()
        if (empresas.isEmpty()) {
            println "Nenhuma empresa cadastrada. (•◡•) /"
        } else {
            println "✦•····· Lista de Empresas ·····•✦"
            empresas.each { empresa ->
                Usuario usuario = usuarioDAO.listarUsuarioPorId(empresa.usuarioId)
                Localizacao localizacao = localizacaoDAO.listarLocalizacoesPorId(empresa.localizacaoId)

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

        if (usuarioDAO.emailExiste(email)) {
            println "Email já cadastrado. Tente outro email. (•◡•) /"
            return
        }

        Empresa empresa = new Empresa()
        empresa.empresaNome = nome
        empresa.cnpj = cnpj

        empresaDAO.salvarEmpresa(empresa, email, senha, descricao, cep, pais)
        println "Empresa cadastrada com sucesso! （っ＾▿＾）"
    }

    def editarEmpresa() {
        listarEmpresas()

        print "Informe o ID da empresa que deseja atualizar: "
        int empresaIdParaEditar = scanner.nextInt()
        scanner.nextLine()

        Empresa empresaPorId = empresaDAO.listarEmpresaPorId(empresaIdParaEditar)
        if (empresaPorId == null) {
            println "Empresa não encontrada. (╥﹏╥)"
            return
        }

        Usuario usuario = usuarioDAO.listarUsuarioPorId(empresaPorId.usuarioId)
        Localizacao localizacao = localizacaoDAO.listarLocalizacoesPorId(empresaPorId.localizacaoId)

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

        boolean sucessoEmpresa = empresaDAO.editarEmpresa(empresaPorId)
        boolean sucessoUsuario = usuarioDAO.editarUsuario(usuario)
        boolean sucessoLocalizacao = localizacaoDAO.editarLocalizacao(localizacao, empresaPorId.empresaId)

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

        boolean sucesso = empresaDAO.excluirEmpresa(idEmpresaDeletar);
        if (sucesso) {
            println("Empresa deletada com sucesso! （っ＾▿＾）");
        } else {
            println("Erro ao deletar empresa. (╥﹏╥)");
        }
    }
}
