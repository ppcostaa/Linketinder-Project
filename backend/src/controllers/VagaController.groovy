package controllers

import infra.CompetenciaRepository
import infra.VagaRepository
import model.Competencia
import model.Vaga

class VagaController {
    Scanner scanner = new Scanner(System.in)
    VagaRepository vagaRepository = new VagaRepository()
    CompetenciaRepository competenciaRepository = new CompetenciaRepository()

    void salvarVaga() {
        println "Informe os dados da Vaga:"
        print "Título: "
        String titulo = scanner.nextLine()
        print "Descrição: "
        String descricao = scanner.nextLine()
        print "Estado: "
        String estado = scanner.nextInt()
        print "Cidade: "
        String cidade = scanner.nextInt()
        scanner.nextLine()
        List<Competencia> competenciasDisponiveis = competenciaRepository.listarCompetencias()
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
        vagaRepository.salvarVaga()
    }

    def index() {
        List<Vaga> vagas = vagaRepository.listarVagas()
        if (vagas.isEmpty()) {
            println "Nenhuma vaga cadastrada. (•◡•) /"
        } else {
            println("✦•····· Lista de Vagas ·····•✦");
            vagas.each { vaga ->
                println "ID: ${vaga.vagaId}, \n" +
                        "Título: ${vaga.titulo} \n" +
                        "Estado: ${vaga.estado}, \n" +
                        "Cidade: ${vaga.cidade}, \n" +
                        "Competências: ${vaga.competenciasRequeridas}, \n" +
                        "Descrição: ${vaga.descricao}, \n" +
                        "✦•·····•✦•·····•✦"
            }
        }
    }

    def editarVaga(Vaga vaga) {
        List<Vaga> vagas = vagaRepository.listarVagas()
        if (vagas.isEmpty()) {
            println "Nenhuma vaga cadastrada para atualizar. (•◡•) /"
        }

        println("✦•····· Lista de Vagas ·····•✦");
        vagas.each { listarVaga ->
            println "ID: ${vaga.vagaId}, \n" +
                    "Título: ${vaga.titulo} \n" +
                    "Estado: ${vaga.estado}, \n" +
                    "Cidade: ${vaga.cidade}, \n" +
                    "Competências: ${vaga.competenciasRequeridas}, \n" +
                    "Descrição: ${vaga.descricao}, \n" +
                    "✦•·····•✦•·····•✦"
        }

        print "Informe o ID da vaga que deseja atualizar: "
        int idVaga = scanner.nextInt()
        scanner.nextLine()

        Vaga vagaParaEditar = vagaRepository.listarVagaPorId(idVaga)
        if (vagaParaEditar == null) {
            println "Vaga não encontrada. (╥﹏╥)"
        }

        println "\nO que você deseja atualizar?"
        println "1. Título"
        println "2. Estado"
        println "3. Cidade"
        println "4. Competências"
        println "5. Descrição"
        print "(Digite os números separados por vírgula): "
        String opcoes = scanner.nextLine()

        List<Integer> opcoesSelecionadas = opcoes.split(',').collect { it.trim().toInteger() }
        List<Integer> opcoesValidas = [1, 2, 3, 4, 5]


        if (opcoesSelecionadas.any { !opcoesValidas.contains(it) }) {
            println "Opção inválida! Escolha números entre 1 e 5. (•◡•) /"
        }

        if (opcoesSelecionadas.contains(1)) {
            print "Digite o novo título: "
            vagaParaEditar.titulo = scanner.nextLine()
        }
        if (opcoesSelecionadas.contains(2)) {
            print "Digite o novo estado: "
            vagaParaEditar.estado = scanner.nextLine()
        }
        if (opcoesSelecionadas.contains(3)) {
            print "Digite o novo cidade: "
            vagaParaEditar.cidade = scanner.nextLine()
        }
        if (opcoesSelecionadas.contains(4)) {
            List<Competencia> competenciasDisponiveis = competenciaRepository.listarCompetencias()
            println "Competências disponíveis:"
            competenciasDisponiveis.each { competencia ->
                println "${competencia.competenciaId}: ${competencia.competenciaNome}"
            } print "Digite os IDs das competências separados por vírgula: "
            String input = scanner.nextLine()
            List<Integer> idsCompetencias = input.split(',').collect { it.trim().toInteger() }
            List<Competencia> competenciasSelecionadas = competenciasDisponiveis.findAll { competencia ->
                idsCompetencias.contains(competencia.competenciaId)
            }

            if (competenciasSelecionadas.isEmpty()) {
                println "Nenhuma competência válida selecionada."
                return
            }
            vagaParaEditar.competenciasRequeridas = competenciasSelecionadas
        }
        if (opcoesSelecionadas.contains(5)) {
            print "Digite a nova Descrição: "
            vagaParaEditar.descricao = scanner.nextLine()
        }

        boolean sucessoVaga = vagaRepository.editarVaga(vaga)

        if (sucessoVaga) {
            println "Vaga atualizada com sucesso! （っ＾▿＾）"
        } else {
            println "Erro ao atualizar vaga. (╥﹏╥)"
        }
    }

    def excluirVaga() {
        List<Vaga> vagas = vagaRepository.listarVagas()
        if (vagas.isEmpty()) {
            println "Nenhuma vaga cadastrada para deletar. (•◡•) /"
        }

        println("✦•····· Lista de Vagas ·····•✦");
        vagas.each { vaga ->
            println "ID: ${vaga.vagaId}, \n" +
                    "Título: ${vaga.titulo} \n" +
                    "Estado: ${vaga.estado}, \n" +
                    "Cidade: ${vaga.cidade}, \n" +
                    "Competências: ${vaga.competenciasRequeridas}, \n" +
                    "Descrição: ${vaga.descricao}, \n" +
                    "✦•·····•✦•·····•✦"
        }

        print "Informe o ID da vaga que deseja deletar: "
        int idVagaDeletar = scanner.nextInt()
        scanner.nextLine()
        boolean sucesso = vagaRepository.excluirVagaMenu(idVagaDeletar)
        if (sucesso) {
            println "Vaga deletada com sucesso!（っ＾▿＾）"
        } else {
            println "Erro ao deletar vaga. (╥﹏╥)"
        }
    }
}