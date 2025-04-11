package controllers

import infra.CompetenciaRepository
import model.Competencia

class CompetenciaController {
    CompetenciaRepository competenciaRepository = new CompetenciaRepository()
    Scanner scanner = new Scanner(System.in)

    def index() {
        List<Competencia> competencias = competenciaRepository.listarCompetencias()
        if (competencias.isEmpty()) {
            println "Nenhuma competência cadastrada. (•◡•) /"
        } else {
            println("✦•····· Lista de Competências ·····•✦")
            competencias.each { competencia ->
                println "ID: ${competencia.competenciaId}, Nome: ${competencia.competenciaNome}"
            }
            println "✦•·····•✦•·····•✦"
        }
    }

    def editarCompetencia() {
        List<Competencia> competencias = competenciaRepository.listarCompetencias()
        if (competencias.isEmpty()) {
            println "Nenhuma competência cadastrada para atualizar. (•◡•) /"
            return
        }

        println("✦•····· Lista de Competências ·····•✦")
        competencias.each { competencia ->
            println "ID: ${competencia.competenciaId}, \n" +
                    "Nome: ${competencia.competenciaNome} \n" +
                    "✦•·····•✦•·····•✦"
        }

        print "Informe o ID da competência que deseja atualizar: "
        int idCompetencia = scanner.nextInt()
        scanner.nextLine()

        Competencia competenciaParaEditar = competenciaRepository.listarCompetenciasPorId(idCompetencia)

        if (competenciaParaEditar == null) {
            println "Competência não encontrada. (╥﹏╥)"
            return
        }

        print "Digite o novo nome: "
        String novoNome = scanner.nextLine()

        Competencia competenciaAtualizada = new Competencia(
                competenciaId: competenciaParaEditar.competenciaId,
                competenciaNome: novoNome
        )

        boolean sucessoCompetencia = competenciaRepository.editarCompetencia(competenciaAtualizada)

        if (sucessoCompetencia) {
            println "Competência atualizada com sucesso! （っ＾▿＾）"
        } else {
            println "Erro ao atualizar competência. (╥﹏╥)"
        }
    }

    def salvarCompetencia() {
        println "Informe os dados da Competência:"
        print "Nome: "
        String nome = scanner.nextLine()
        Competencia novaCompetencia = new Competencia(competenciaNome: nome)

        Competencia competenciaSalva = competenciaRepository.salvarCompetencia(novaCompetencia)

        if (competenciaSalva) {
            println "Competência '${competenciaSalva.competenciaNome}' cadastrada com sucesso! ID: ${competenciaSalva.competenciaId}"
        } else {
            println "Erro ao cadastrar competência."
        }

    }

    def excluirCompetencia() {
        List<Competencia> competencias = competenciaRepository.listarCompetencias()
        if (competencias.isEmpty()) {
            println "Nenhuma competência cadastrada para deletar. (•◡•) /"
            return
        }

        println("✦•····· Lista de Competências ·····•✦")
        competencias.each { competencia ->
            println "ID: ${competencia.competenciaId}, \n" +
                    "Nome: ${competencia.competenciaNome} \n" +
                    "✦•·····•✦•·····•✦"
        }

        print "Informe o ID da competência que deseja deletar: "
        int idCompetenciaDeletar = scanner.nextInt()
        scanner.nextLine()

        boolean sucesso = competenciaRepository.excluirCompetencia(idCompetenciaDeletar)
        if (sucesso) {
            println "Competência deletada com sucesso!（っ＾▿＾）"
            return
        } else {
            println "Erro ao deletar competência. (╥﹏╥)"
            return
        }
    }
}
