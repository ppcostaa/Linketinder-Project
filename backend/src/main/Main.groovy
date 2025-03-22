package main

import model.Candidato
import model.Empresa
import model.Localizacao
import model.Usuario
import utils.GerenciadorCandidato
import utils.GerenciadorEmpresa
import utils.GerenciadorLocalizacao
import utils.GerenciadorUsuario

import java.text.SimpleDateFormat

static void main(String[] args) {
    def sistema = new Sistema()
    GerenciadorCandidato gerenciadorCandidato = new GerenciadorCandidato()
    GerenciadorLocalizacao gerenciadorLocalizacao = new GerenciadorLocalizacao()
    GerenciadorEmpresa gerenciadorEmpresa = new GerenciadorEmpresa()
    GerenciadorUsuario gerenciadorUsuario = new GerenciadorUsuario()
    Scanner scanner = new Scanner(System.in)

    while (true) {
        println "\n======= Linketinder ======="
        println "1. Cadastrar Candidato"
        println "2. Cadastrar Empresa"
        println "3. Listar Candidatos"
        println "4. Listar Empresas"
        println "5. Editar Candidato"
        println "6. Editar Empresa"
        println "7. Remover Candidato"
        println "8. Remover Empresa"
        println "9. Sair"
        print "Escolha uma opção: "

        String opcao = scanner.nextLine()

        switch (opcao) {
            case "1":
                println "Informe os dados do Candidato:"
                print "Nome: "
                String nome = scanner.nextLine()
                print "Sobrenome: "
                String sobrenome = scanner.nextLine()
                print "Email: "
                String email = scanner.nextLine()
                print "Senha: "
                String senha = scanner.nextLine()
                print "CEP: "
                String cep = scanner.nextLine()
                print "País: "
                String pais = scanner.nextLine()
                print "CPF: "
                String cpf = scanner.nextLine()
                print "Data de Nascimento (dd/MM/yyyy): "
                String dataNascimentoStr = scanner.nextLine()
                print "Competências (separadas por vírgula): "
                List<String> competencias = scanner.nextLine().split(',').collect { it.trim() }
                print "Descrição: "
                String descricao = scanner.nextLine()

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy")
                Date dataNascimento = null
                try {
                    dataNascimento = sdf.parse(dataNascimentoStr)
                } catch (Exception e) {
                    println "Formato de data inválido! A data deve estar no formato dd/MM/yyyy."
                    return
                }

                if (gerenciadorUsuario.emailExists(email)) {
                    System.out.println("Email já cadastrado. Tente outro email.")
                    return
                }

                def candidato = new Candidato([idCandidato: '0', nome: nome, sobrenome: sobrenome, cpf: cpf, dataNascimento: dataNascimento, competencias: competencias])
                gerenciadorCandidato.create(candidato, email, senha, descricao, cep, pais)
                println "Candidato cadastrado com sucesso!"
                break

            case "2":
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

                if (gerenciadorUsuario.emailExists(email)) {
                    System.out.println("Email já cadastrado. Tente outro email.")
                    return
                }

                def empresa = new Empresa([idEmpresa: '0', nome: nome, cnpj: cnpj])
                gerenciadorEmpresa.create(empresa, email, senha, descricao, cep, pais)
                println "Empresa cadastrada com sucesso!"
                break
            case "3":
                List<Candidato> candidatos = gerenciadorCandidato.findAll()
                if (candidatos.isEmpty()) {
                    println "Nenhum candidato cadastrado."
                } else {
                    println "Lista de Candidatos:"
                    candidatos.each { candidato ->
                        Usuario usuario = gerenciadorUsuario.findById(candidato.idUsuario)
                        Localizacao localizacao = gerenciadorLocalizacao.findById(candidato.idLocalizacao)
                        println "ID: ${candidato.idCandidato}, \n" +
                                "Nome: ${candidato.nome} \n" +
                                "Sobrenome: ${candidato.sobrenome}, \n" +
                                "Email: ${usuario.email}, \n" +
                                "CEP: ${localizacao.cep}, \n" +
                                "País: ${localizacao.pais}, \n" +
                                "CPF: ${candidato.cpf}, \n" +
                                "Data de Nascimento: ${candidato.dataNascimento}, \n" +
                                "Competências: ${candidato.competencias}, \n" +
                                "Descrição: ${usuario.descricao} \n"
                    }
                }
                break
            case "4":
                List<Empresa> empresas = gerenciadorEmpresa.findAll()
                if (empresas.isEmpty()) {
                    println "Nenhuma empresa cadastrada."
                } else {
                    println "Lista de Empresas:"
                    empresas.each { empresa ->
                        Usuario usuario = gerenciadorUsuario.findById(empresa.idUsuario)
                        Localizacao localizacao = gerenciadorLocalizacao.findById(empresa.idLocalizacao)
                        println "ID: ${empresa.idEmpresa}, \n" +
                                "Nome: ${empresa.nomeEmpresa} \n" +
                                "CNPJ: ${empresa.cnpj}, \n" +
                                "Email: ${usuario.email}, \n" +
                                "CEP: ${localizacao.cep}, \n" +
                                "País: ${localizacao.pais}, \n" +
                                "Descrição: ${usuario.descricao} \n"
                    }
                }
                break
            case "5":
                List<Candidato> candidatos = gerenciadorCandidato.findAll()
                if (candidatos.isEmpty()) {
                    println "Nenhum candidato cadastrado para atualizar."
                    break
                }

                println "Lista de Candidatos:"
                candidatos.each { candidato ->
                    Usuario usuario = gerenciadorUsuario.findById(candidato.idUsuario)
                    Localizacao localizacao = gerenciadorLocalizacao.findById(candidato.idLocalizacao)

                    println "ID: ${candidato.idCandidato}, \n" +
                            "Nome: ${candidato.nome} \n" +
                            "Sobrenome: ${candidato.sobrenome}, \n" +
                            "Email: ${usuario.email}, \n" +
                            "CEP: ${localizacao.cep}, \n" +
                            "País: ${localizacao.pais}, \n" +
                            "CPF: ${candidato.cpf}, \n" +
                            "Data de Nascimento: ${candidato.dataNascimento}, \n" +
                            "Competências: ${candidato.competencias}, \n" +
                            "Descrição: ${usuario.descricao} \n"
                }

                print "Informe o ID do candidato que deseja atualizar: "
                int idCandidato = scanner.nextInt()
                scanner.nextLine()

                Candidato candidato = gerenciadorCandidato.findById(idCandidato)
                if (candidato == null) {
                    println "Candidato não encontrado."
                    break
                }

                Usuario usuario = gerenciadorUsuario.findById(candidato.idUsuario)
                Localizacao localizacao = gerenciadorLocalizacao.findById(candidato.idLocalizacao)

                println "\nO que você deseja atualizar?"
                println "1. Nome"
                println "2. Sobrenome"
                println "3. Email"
                println "4. Senha"
                println "5. CEP"
                println "6. País"
                println "7. CPF"
                println "8. Data de Nascimento"
                println "9. Competências"
                println "10. Descrição"
                print "(Digite os números separados por vírgula): "
                String opcoes = scanner.nextLine()

                List<Integer> opcoesSelecionadas = opcoes.split(',').collect { it.trim().toInteger() }
                List<Integer> opcoesValidas = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

                if (opcoesSelecionadas.any { !opcoesValidas.contains(it) }) {
                    println "Opção inválida! Escolha números entre 1 e 10."
                    break
                }

                if (opcoesSelecionadas.contains(1)) {
                    print "Digite o novo nome: "
                    candidato.nome = scanner.nextLine()
                }
                if (opcoesSelecionadas.contains(2)) {
                    print "Digite o novo sobrenome: "
                    candidato.sobrenome = scanner.nextLine()
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
                    print "Digite o novo CPF: "
                    candidato.cpf = scanner.nextLine()
                }
                if (opcoesSelecionadas.contains(8)) {
                    print "Digite a nova data de nascimento (dd/MM/yyyy): "
                    String novaDataNascimentoStr = scanner.nextLine()
                    try {
                        candidato.dataNascimento = new SimpleDateFormat("dd/MM/yyyy").parse(novaDataNascimentoStr)
                    } catch (Exception e) {
                        println "Formato de data inválido! A data deve estar no formato dd/MM/yyyy."
                        break
                    }
                }
                if (opcoesSelecionadas.contains(9)) {
                    print "Digite as novas competências (separadas por vírgula): "
                    candidato.competencias = scanner.nextLine().split(',').collect { it.trim() }
                }
                if (opcoesSelecionadas.contains(10)) {
                    print "Digite a nova Descrição: "
                    usuario.descricao = scanner.nextLine()
                }

                boolean sucessoCandidato = gerenciadorCandidato.update(candidato)
                boolean sucessoUsuario = gerenciadorUsuario.update(usuario)
                boolean sucessoLocalizacao = gerenciadorLocalizacao.update(localizacao, candidato.idCandidato)

                if (sucessoCandidato && sucessoUsuario && sucessoLocalizacao) {
                    println "Candidato atualizado com sucesso!"
                } else {
                    println "Erro ao atualizar candidato."
                }
                break
            case "6":
                List<Empresa> empresas = gerenciadorEmpresa.findAll()
                if (empresas.isEmpty()) {
                    println "Nenhuma empresa cadastrada para atualizar."
                    break
                }

                println "Lista de Empresas:"
                empresas.each { empresa ->
                    Usuario usuario = gerenciadorUsuario.findById(empresas.idUsuario)
                    Localizacao localizacao = gerenciadorLocalizacao.findById(empresas.idLocalizacao)

                    println "ID: ${empresas.idEmpresa}, \n" +
                            "Nome: ${empresas.nomeEmpresa} \n" +
                            "CNPJ: ${empresas.cnpj} \n" +
                            "Email: ${usuario.email}, \n" +
                            "CEP: ${localizacao.cep}, \n" +
                            "País: ${localizacao.pais}, \n" +
                            "Descrição: ${usuario.descricao} \n"
                }

                print "Informe o ID da empresa que deseja atualizar: "
                int idEmpresa = scanner.nextInt()
                scanner.nextLine()

                Empresa empresa = gerenciadorEmpresa.findById(idEmpresa)
                if (empresa == null) {
                    println "Empresa não encontrada."
                    break
                }

                Usuario usuario = gerenciadorUsuario.findById(empresa.idUsuario)
                Localizacao localizacao = gerenciadorLocalizacao.findById(empresa.idLocalizacao)

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
                    println "Opção inválida! Escolha números entre 1 e 7."
                    break
                }

                if (opcoesSelecionadas.contains(1)) {
                    print "Digite o novo nome: "
                    empresa.nomeEmpresa = scanner.nextLine()
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

                boolean sucessoEmpresa = gerenciadorEmpresa.update(empresa)
                boolean sucessoUsuario = gerenciadorUsuario.update(usuario)
                boolean sucessoLocalizacao = gerenciadorLocalizacao.update(localizacao, empresa.idEmpresa)

                if (sucessoEmpresa && sucessoUsuario && sucessoLocalizacao) {
                    println "Empresa atualizada com sucesso!"
                } else {
                    println "Erro ao atualizar empresa."
                }
                break
            case "7":
                List<Candidato> candidatos = gerenciadorCandidato.findAll()
                if (candidatos.isEmpty()) {
                    println "Nenhum candidato cadastrado para deletar."
                    break
                }

                println "Lista de Candidatos:"
                candidatos.each { candidato ->
                    Usuario usuario = gerenciadorUsuario.findById(candidato.idUsuario)
                    Localizacao localizacao = gerenciadorLocalizacao.findById(candidato.idLocalizacao)

                    println "ID: ${candidato.idCandidato}, \n" +
                            "Nome: ${candidato.nome} \n" +
                            "Sobrenome: ${candidato.sobrenome}, \n" +
                            "Email: ${usuario.email}, \n" +
                            "CEP: ${localizacao.cep}, \n" +
                            "País: ${localizacao.pais}, \n" +
                            "CPF: ${candidato.cpf}, \n" +
                            "Data de Nascimento: ${candidato.dataNascimento}, \n" +
                            "Competências: ${candidato.competencias}, \n" +
                            "Descrição: ${usuario.descricao} \n"
                }

                print "Informe o ID do candidato que deseja deletar: "
                int idCandidatoDeletar = scanner.nextInt()
                scanner.nextLine()
                boolean sucesso = gerenciadorCandidato.delete(idCandidatoDeletar)
                if (sucesso) {
                    println "Candidato deletado com sucesso!"
                } else {
                    println "Erro ao deletar candidato."
                }
                break
            case "8":
                List<Empresa> empresas = gerenciadorEmpresa.findAll()
                if (empresas.isEmpty()) {
                    println "Nenhuma empresa cadastrada para deletar."
                    break
                }

                println "Lista de Empresas:"
                empresas.each { empresa ->
                    Usuario usuario = gerenciadorUsuario.findById(empresa.idUsuario)
                    Localizacao localizacao = gerenciadorLocalizacao.findById(empresa.idLocalizacao)

                    println "ID: ${empresas.idEmpresa}, \n" +
                            "Nome: ${empresas.nomeEmpresa} \n" +
                            "CNPJ: ${empresas.cnpj} \n" +
                            "Email: ${usuario.email}, \n" +
                            "CEP: ${localizacao.cep}, \n" +
                            "País: ${localizacao.pais}, \n" +
                            "Descrição: ${usuario.descricao} \n"
                }

                print "Informe o ID da Empresa que deseja deletar: "
                int idEmpresaDeletar = scanner.nextInt()
                scanner.nextLine()
                boolean sucesso = gerenciadorEmpresa.delete(idEmpresaDeletar)
                if (sucesso) {
                    println "Empresa deletada com sucesso!"
                } else {
                    println "Erro ao deletar empresa."
                }
                break
            case "9":
                println "Saindo..."
                return
            default:
                println "Opção inválida! Tente novamente."
        }
    }
}