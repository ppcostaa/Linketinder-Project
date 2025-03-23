package controllers

import infra.CandidatoRepository
import infra.CompetenciaRepository
import infra.LocalizacaoRepository
import infra.UsuarioRepository
import model.Candidato
import model.Competencia
import model.Localizacao
import model.Usuario

import java.text.SimpleDateFormat

class CandidatoController {
    Scanner scanner = new Scanner(System.in)
    CompetenciaRepository competenciaRepository = new CompetenciaRepository()
    CandidatoRepository candidatoRepository = new CandidatoRepository()
    UsuarioRepository usuarioRepository = new UsuarioRepository()
    LocalizacaoRepository localizacaoRepository = new LocalizacaoRepository()

    def index() {
        List<Candidato> candidatos = candidatoRepository.listarCandidatos()
        if (candidatos.isEmpty()) {
            println "Nenhum candidato cadastrado. (•◡•) /"
        } else {
            println("✦•····· Lista de Candidatos ·····•✦");
            candidatos.each { candidato ->
                Usuario usuario = usuarioRepository.listarUsuariosPorId(candidato.usuarioId)
                Localizacao localizacao = localizacaoRepository.listarLocalizacaoPorId(candidato.localizacaoId)
                println "ID: ${candidato.candidatoId}, \n" +
                        "Nome: ${candidato.nome} \n" +
                        "Sobrenome: ${candidato.sobrenome}, \n" +
                        "Email: ${usuario.email}, \n" +
                        "CEP: ${localizacao.cep}, \n" +
                        "País: ${localizacao.pais}, \n" +
                        "CPF: ${candidato.cpf}, \n" +
                        "Data de Nascimento: ${candidato.dataNascimento}, \n" +
                        "Competências: ${candidato.competencias}, \n" +
                        "Descrição: ${usuario.descricao} \n" +
                        "✦•·····•✦•·····•✦"
            }
        }
    }

    def salvarCandidatoMenu() {
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
        List<Competencia> competenciasDisponiveis = competenciaRepository.listarCompetencias()
        println "Competências disponíveis:"
        competenciasDisponiveis.each { competencia ->
            println "${competencia.competenciaId}: ${competencia.competenciaNome}"
        }
        print "Digite os IDs das competências separados por vírgula: "
        String input = scanner.nextLine()
        List<Integer> idsCompetencias = input.split(',').collect { it.trim().toInteger() }
        print "Descrição: "
        String descricao = scanner.nextLine()

        List<Competencia> competenciasSelecionadas = competenciasDisponiveis.findAll { competencia ->
            idsCompetencias.contains(competencia.competenciaId)
        }

        if (competenciasSelecionadas.isEmpty()) {
            println "Nenhuma competência válida selecionada."
            return
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy")
        Date dataNascimento = null
        try {
            dataNascimento = sdf.parse(dataNascimentoStr)
        } catch (Exception e) {
            println "Formato de data inválido! A data deve estar no formato dd/MM/yyyy. (•◡•) /"
            return
        }
        UsuarioRepository usuarioRepository = new UsuarioRepository()

        if (usuarioRepository.emailExiste(email)) {
            println "Email já cadastrado. Tente outro email. (•◡•) /"
            return
        }

        def candidato = new Candidato(
                nome: nome,
                sobrenome: sobrenome,
                cpf: cpf,
                dataNascimento: dataNascimento,
                competencias: competenciasSelecionadas
        )

        candidatoRepository.salvarCandidato(candidato, email, senha, descricao, cep, pais)
        println "Candidato cadastrado com sucesso! （っ＾▿＾）"
    }

    def editarCandidatoMenu() {
        List<Candidato> candidatos = candidatoRepository.listarCandidatos()
        if (candidatos.isEmpty()) {
            println "Nenhum candidato cadastrado para atualizar. (•◡•) /"
            return
        }

        println("✦•····· Lista de Candidatos ·····•✦")
        candidatos.each { candidato ->
            if (candidato == null) {
                println "Candidato nulo encontrado. Verifique os dados no banco de dados."
                return
            }

            Usuario usuario = usuarioRepository.listarUsuariosPorId(candidato.usuarioId)
            Localizacao localizacao = localizacaoRepository.listarLocalizacaoPorId(candidato.localizacaoId)

            println "ID: ${candidato.candidatoId}, \n" +
                    "Nome: ${candidato.nome} \n" +
                    "Sobrenome: ${candidato.sobrenome}, \n" +
                    "Email: ${usuario.email}, \n" +
                    "CEP: ${localizacao.cep}, \n" +
                    "País: ${localizacao.pais}, \n" +
                    "CPF: ${candidato.cpf}, \n" +
                    "Data de Nascimento: ${candidato.dataNascimento}, \n" +
                    "Competências: ${candidato.competencias}, \n" +
                    "Descrição: ${usuario.descricao} \n" +
                    "✦•·····•✦•·····•✦"
        }

        print "Informe o ID do candidato que deseja atualizar: "
        int idCandidato = scanner.nextInt()
        scanner.nextLine()

        Candidato candidatoParaEditar = candidatoRepository.listarCandidatoPorId(idCandidato)
        if (candidatoParaEditar == null) {
            println "Candidato não encontrado. (╥﹏╥)"
            return
        }

        Usuario usuario = usuarioRepository.listarUsuariosPorId(candidatoParaEditar.usuarioId)
        Localizacao localizacao = localizacaoRepository.listarLocalizacaoPorId(candidatoParaEditar.localizacaoId)

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
            println "Opção inválida! Escolha números entre 1 e 10. (•◡•) /"
            return
        }

        if (opcoesSelecionadas.contains(1)) {
            print "Digite o novo nome: "
            candidatoParaEditar.nome = scanner.nextLine()
        }
        if (opcoesSelecionadas.contains(2)) {
            print "Digite o novo sobrenome: "
            candidatoParaEditar.sobrenome = scanner.nextLine()
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
            candidatoParaEditar.cpf = scanner.nextLine()
        }
        if (opcoesSelecionadas.contains(8)) {
            print "Digite a nova data de nascimento (dd/MM/yyyy): "
            String novaDataNascimentoStr = scanner.nextLine()
            try {
                candidatoParaEditar.dataNascimento = new SimpleDateFormat("dd/MM/yyyy").parse(novaDataNascimentoStr)
            } catch (Exception e) {
                println "Formato de data inválido! A data deve estar no formato dd/MM/yyyy. (•◡•) /"
            }
        }
        if (opcoesSelecionadas.contains(9)) {
            // Passando o ID do candidato como argumento, se necessário
            List<Competencia> competenciasDisponiveis = competenciaRepository.listarCompetencias(candidatoParaEditar.candidatoId)
            println "Competências disponíveis:"
            competenciasDisponiveis.each { competencia ->
                println "${competencia.competenciaId}: ${competencia.competenciaNome}"
            }
            print "Digite os IDs das competências separados por vírgula: "
            String input = scanner.nextLine()
            List<Integer> idsCompetencias = input.split(',').collect { it.trim().toInteger() }
            List<Competencia> competenciasSelecionadas = competenciasDisponiveis.findAll { competencia ->
                idsCompetencias.contains(competencia.competenciaId)
            }

            if (competenciasSelecionadas.isEmpty()) {
                println "Nenhuma competência válida selecionada."
                return
            }
            candidatoParaEditar.competencias = competenciasSelecionadas
        }
        if (opcoesSelecionadas.contains(10)) {
            print "Digite a nova Descrição: "
            usuario.descricao = scanner.nextLine()
        }

        boolean sucessoCandidato = candidatoRepository.editarCandidato(candidatoParaEditar)
        boolean sucessoUsuario = usuarioRepository.editarUsuario(usuario)
        boolean sucessoLocalizacao = localizacaoRepository.editarLocalizacao(localizacao)

        if (sucessoCandidato && sucessoUsuario && sucessoLocalizacao) {
            println "Candidato atualizado com sucesso! （っ＾▿＾）"
        } else {
            println "Erro ao atualizar candidato. (╥﹏╥)"
        }
    }

    def excluirCandidatoMenu() {
        List<Candidato> candidatos = candidatoRepository.listarCandidatos()
        if (candidatos.isEmpty()) {
            println "Nenhum candidato cadastrado para deletar. (•◡•) /"
        }

        println("✦•····· Lista de Candidatos ·····•✦");
        candidatos.each { candidato ->
            Usuario usuario = usuarioRepository.listarUsuariosPorId(candidato.usuarioId)
            Localizacao localizacao = localizacaoRepository.listarLocalizacaoPorId(candidato.localizacaoId)

            println "ID: ${candidato.candidatoId}, \n" +
                    "Nome: ${candidato.nome} \n" +
                    "Sobrenome: ${candidato.sobrenome}, \n" +
                    "Email: ${usuario.email}, \n" +
                    "CEP: ${localizacao.cep}, \n" +
                    "País: ${localizacao.pais}, \n" +
                    "CPF: ${candidato.cpf}, \n" +
                    "Data de Nascimento: ${candidato.dataNascimento}, \n" +
                    "Competências: ${candidato.competencias}, \n" +
                    "Descrição: ${usuario.descricao} \n" +
                    "✦•·····•✦•·····•✦"
        }

        print "Informe o ID do candidato que deseja deletar: "
        int idCandidatoDeletar = scanner.nextInt()
        scanner.nextLine()
        boolean sucesso = candidatoRepository.excluirCandidato(idCandidatoDeletar)
        if (sucesso) {
            println "Candidato deletado com sucesso!（っ＾▿＾）"
        } else {
            println "Erro ao deletar candidato. (╥﹏╥)"
        }
    }
}
