package controllers

import infra.CompetenciaRepository
import model.Competencia

class CompetenciaController {
    CompetenciaRepository competenciaRepository = new CompetenciaRepository()
    Scanner scanner = new Scanner(System.in)

    def index() {
        List<Competencia> competencias = competenciaRepository.listarCompetencias()
        if (competencias.isEmpty()) {
            println "Nenhuma competência cadastrado. (•◡•) /"
        } else {
            println("✦•····· Lista de Competências ·····•✦");
            competencias.each { candidato ->
                println "ID: ${competencias.competenciaId}, \n" +
                        "Nome: ${competencias.competenciaNome} \n" +
                        "✦•·····•✦•·····•✦"
            }
        }
    }

    def editarCompetencia(Competencia competencia) {
        List<Competencia> competencias = competenciaRepository.listarCompetencias()
        if (competencias.isEmpty()) {
            println "Nenhuma competência cadastrada para atualizar. (•◡•) /"
        }
        println("✦•····· Lista de Competências ·····•✦");
        competencias.each { x ->
            println "ID: ${competencia.competenciaId}, \n" +
                    "Nome: ${competencia.competenciaNome} \n" +
                    "✦•·····•✦•·····•✦"
        }
        print "Informe o ID da competência que deseja atualizar: "
        int idCompetencia = scanner.nextInt()
        scanner.nextLine()

        CompetenciaController competenciaParaEditar = competenciaRepository.listarCompetenciasPorId(idCompetencia)
        if (competenciaParaEditar == null) {
            println "Competência não encontrada. (╥﹏╥)"
        } else {
            print "Digite o novo nome: "
            competencia.competenciaNome = scanner.nextLine()
        }

        boolean sucessoCompetencia = competenciaRepository.editarCompetencia(competencia)

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
        competenciaRepository.salvarCompetencia()
    }

    def excluirCompetencia() {
        List<Competencia> competencias = competenciaRepository.listarCompetencias()
        if (competencias.isEmpty()) {
            println "Nenhuma competência cadastrada para deletar. (•◡•) /"
        }

        println("✦•····· Lista de Competências ·····•✦");
        competencias.each { competencia ->
            println "ID: ${competencias.competenciaId}, \n" +
                    "Nome: ${competencias.competenciaNome} \n" +
                    "✦•·····•✦•·····•✦"
        }

        print "Informe o ID da competência que deseja deletar: "
        int idCompetenciaDeletar = scanner.nextInt()
        scanner.nextLine()
        boolean sucesso = competenciaRepository.excluirCompetencia(idCompetenciaDeletar)
        if (sucesso) {
            println "Competência deletada com sucesso!（っ＾▿＾）"
        } else {
            println "Erro ao deletar competência. (╥﹏╥)"
        }
    }

}
