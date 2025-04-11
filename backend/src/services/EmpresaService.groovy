package services

import infra.EmpresaRepository

class EmpresaService {
    final EmpresaRepository empresaRepository

    EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository
    }
}
