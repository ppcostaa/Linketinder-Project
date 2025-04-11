package services

import infra.CompetenciaRepository

class CompetenciaService {
    final CompetenciaRepository competenciaRepository

    CompetenciaService(CompetenciaRepository competenciaRepository) {
        this.competenciaRepository = competenciaRepository
    }
}
