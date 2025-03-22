package controllers

import infra.CompetenciaRepository
import model.Competencia

class CompetenciaController {
    CompetenciaRepository competenciaRepository = new CompetenciaRepository()

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
}
