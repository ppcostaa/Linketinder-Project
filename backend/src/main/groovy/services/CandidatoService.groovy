package services


import repository.CandidatoRepository
import repository.CompetenciaRepository
import repository.LocalizacaoRepository
import repository.UsuarioRepository
import model.Candidato
import model.Competencia
import model.Localizacao
import model.Usuario

import java.text.SimpleDateFormat

class CandidatoService {
    Scanner scanner = new Scanner(System.in)
    CompetenciaRepository competenciaRepository = new CompetenciaRepository()
    CandidatoRepository candidatoRepository = new CandidatoRepository()
    UsuarioRepository usuarioRepository = new UsuarioRepository()
    LocalizacaoRepository localizacaoRepository = new LocalizacaoRepository()
    CompetenciaService competenciaService = new CompetenciaService()

    def listarCandidatos() {
        List<Candidato> candidatos = candidatoRepository.listarCandidatos()
        if (candidatos.isEmpty()) {
            println "Nenhum candidato cadastrado. (•◡•) /"
        } else {
            println("✦•····· Lista de Candidatos ·····•✦");
            candidatos.each { candidato ->
                Usuario usuario = usuarioRepository.listarUsuarioPorId(candidato.usuarioId)
                Localizacao localizacao = localizacaoRepository.listarLocalizacoesPorId(candidato.localizacaoId)
                println "ID: ${candidato.candidatoId}, \n" +
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

    def salvarDataNascimento() {
        print "Data de Nascimento (dd/MM/yyyy): "
        String dataNascimentoStr = scanner.nextLine()
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy")
        sdf.setLenient(false)
        try {
            return sdf.parse(dataNascimentoStr)
        } catch (Exception e) {
            println "Formato de data inválido! A data deve estar no formato dd/MM/yyyy e ser uma data válida. (•◡•) /"
            return null
        }
    }

    def salvarCandidato() {
        println "Informe os dados do Candidato:"
        print "Nome: "
        String nome = scanner.nextLine()
        print "Sobrenome: "
        String sobrenome = scanner.nextLine()
        print "Email: "
        String email = scanner.nextLine()
        if (usuarioRepository.emailExiste(email)) {
            println "Email já cadastrado. Tente outro email. (•◡•) /"
            return
        }
        print "Senha: "
        String senha = scanner.nextLine()
        print "CEP: "
        String cep = scanner.nextLine()
        print "País: "
        String pais = scanner.nextLine()
        print "CPF: "
        String cpf = scanner.nextLine()
        Date dataNascimento = null
        while (dataNascimento == null) {
            dataNascimento = salvarDataNascimento()
            if (dataNascimento == null) {
                println "Por favor, insira uma data de nascimento válida."
            }
        }
        List<Competencia> competencias = competenciaService.salvarCompetenciaPorUsuario()
        print "Descrição: "
        String descricao = scanner.nextLine()

        def candidato = new Candidato(
                nome: nome,
                sobrenome: sobrenome,
                cpf: cpf,
                dataNascimento: dataNascimento,
                competencias: competencias
        )

        candidatoRepository.salvarCandidato(candidato, email, senha, descricao, cep, pais)
        println "Candidato cadastrado com sucesso! （っ＾▿＾）"
    }

    def editarCandidato() {
        listarCandidatos()
        print "Informe o ID do candidato que deseja atualizar: "
        int idCandidato = scanner.nextInt()
        scanner.nextLine()
        Candidato candidatoParaEditar = candidatoRepository.listarCandidatoPorId(idCandidato)
        if (candidatoParaEditar == null) {
            println "Candidato não encontrado. (╥﹏╥)"
            return
        }

        Usuario usuario = usuarioRepository.listarUsuarioPorId(candidatoParaEditar.usuarioId)
        Localizacao localizacao = localizacaoRepository.listarLocalizacoesPorId(candidatoParaEditar.localizacaoId)

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
        opcoesSelecionadas.each { opcao ->
            switch (opcao) {
                case 1:
                    print "Digite o novo nome: "
                    candidatoParaEditar.nome = scanner.nextLine()
                    break
                case 2:
                    print "Digite o novo sobrenome: "
                    candidatoParaEditar.sobrenome = scanner.nextLine()
                    break
                case 3:
                    print "Digite o novo email: "
                    usuario.email = scanner.nextLine()
                    break
                case 4:
                    print "Digite a nova Senha: "
                    usuario.senha = scanner.nextLine()
                    break
                case 5:
                    print "Digite o novo CEP: "
                    localizacao.cep = scanner.nextLine()
                    break
                case 6:
                    print "Digite o novo País: "
                    localizacao.pais = scanner.nextLine()
                    break
                case 7:
                    print "Digite o novo CPF: "
                    candidatoParaEditar.cpf = scanner.nextLine()
                    break
                case 8:
                    print "Digite a nova data de nascimento (dd/MM/yyyy): "
                    String novaDataNascimentoStr = scanner.nextLine()
                    try {
                        candidatoParaEditar.dataNascimento = new SimpleDateFormat("dd/MM/yyyy").parse(novaDataNascimentoStr)
                    } catch (Exception e) {
                        println "Formato de data inválido! A data deve estar no formato dd/MM/yyyy. (•◡•) /"
                    }
                    break
                case 9:
                    println "\nEdição de Competências:"
                    println "1. Adicionar novas competências"
                    println "2. Remover competências existentes"
                    print "Escolha uma opção: "
                    int opcaoCompetencia = scanner.nextInt()
                    scanner.nextLine()

                    if (opcaoCompetencia == 1) {
                        List<Competencia> todasCompetencias = competenciaRepository.listarCompetencias()
                        List<Competencia> competenciasAtuais = candidatoParaEditar.competencias
                        List<Competencia> competenciasDisponiveis = todasCompetencias.findAll { competencia ->
                            !competenciasAtuais.any { it.competenciaId == competencia.competenciaId }
                        }

                        println "Competências disponíveis para adicionar:"
                        competenciasDisponiveis.eachWithIndex { competencia, index ->
                            println "${index + 1}: ${competencia.competenciaNome}"
                        }
                        print "Digite os números das competências que deseja adicionar (separados por vírgula): "
                        String input = scanner.nextLine()
                        List<Integer> indicesSelecionados = input.split(',').collect { it.trim().toInteger() }

                        List<Competencia> competenciasSelecionadas = indicesSelecionados.collect { index ->
                            competenciasDisponiveis[index - 1]
                        }

                        candidatoParaEditar.competencias.addAll(competenciasSelecionadas)
                    } else if (opcaoCompetencia == 2) {
                        println "Competências atuais do candidato:"
                        candidatoParaEditar.competencias.eachWithIndex { competencia, index ->
                            println "${index + 1}: ${competencia.competenciaNome}"
                        }
                        print "Digite os números das competências que deseja remover (separados por vírgula): "
                        String input = scanner.nextLine()
                        List<Integer> indicesSelecionados = input.split(',').collect { it.trim().toInteger() }

                        indicesSelecionados.sort().reverse().each { index ->
                            candidatoParaEditar.competencias.remove(index - 1)
                        }
                    } else {
                        println "Opção inválida!"
                    }
                    break
                case 10:
                    print "Digite a nova Descrição: "
                    usuario.descricao = scanner.nextLine()
                    break
            }
        }

        boolean sucessoCandidato = candidatoRepository.editarCandidato(candidatoParaEditar)
        boolean sucessoUsuario = usuarioRepository.editarUsuario(usuario)
        boolean sucessoLocalizacao = localizacaoRepository.editarLocalizacao(localizacao, localizacao.localizacaoId)

        if (sucessoCandidato && sucessoUsuario && sucessoLocalizacao) {
            println "Candidato atualizado com sucesso! （っ＾▿＾）"
        } else {
            println "Erro ao atualizar candidato. (╥﹏╥)"
        }
    }

    def excluirCandidato() {
        listarCandidatos()
        print "Informe o ID do candidato que deseja deletar: "
        int idCandidatoDeletar = scanner.nextInt()
        scanner.nextLine()

        def candidatos = candidatoRepository.listarCandidatos()
        if (!candidatos.any { it.candidatoId == idCandidatoDeletar }) {
            println "Candidato não encontrado. (╥﹏╥)"
            return
        }

        boolean sucesso = candidatoRepository.excluirCandidato(idCandidatoDeletar)
        if (sucesso) {
            println "Candidato deletado com sucesso!（っ＾▿＾）"
        } else {
            println "Erro ao deletar candidato. (╥﹏╥)"
        }
    }
}
