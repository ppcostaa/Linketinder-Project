package services

import infra.CandidatoRepository

class CandidatoService {
    final CandidatoRepository candidatoRepository

    CandidatoService(CandidatoRepository candidatoRepository) {
        this.candidatoRepository = candidatoRepository
    }
}
