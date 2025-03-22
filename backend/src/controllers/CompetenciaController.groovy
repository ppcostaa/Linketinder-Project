package controllers

import services.CompetenciaService

class CompetenciaController {
    CompetenciaService competenciaService
    def index() {
        respond competenciaService.competenciaRepository.listarCompetencias()
    }
}
