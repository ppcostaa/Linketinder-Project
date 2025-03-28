package services

import infra.VagaRepository

class VagaService {
    final VagaRepository vagaRepository

    VagaService(VagaRepository vagaRepository){
        this.vagaRepository = vagaRepository
    }
}
