package controllers


import infra.EmpresaRepository
import model.Empresa
import services.EmpresaService

class EmpresaController {
    EmpresaService empresaService

    def index() {
        respond empresaService.empresaRepository.listarEmpresas()
    }

    def listarEmpresaPorId(int empresaId) {
        def empresa = empresaService.empresaRepository.listarEmpresaPorId(empresaId)
        if (empresa) {
            respond empresa
        } else {
            render status: 404, text: "Empresa não encontrada"
        }
    }

    def salvarEmpresa() {
        def empresa = new EmpresaRepository()
        if (empresa.salvarEmpresa(flush: true)) {
            respond empresa, status: 201
        } else {
            respond empresa.errors, status: 400
        }
    }

    def editarEmpresa(Empresa empresa) {
        def empresas = empresaService.empresaRepository.listarEmpresaPorId(candidato)
        if (!empresas) {
            render status: 404, text: "Empresa não encontrada"
            return
        }

        empresaService.empresaRepository.properties = params
        if (empresaService.empresaRepository.salvarEmpresa(
                flush: true)) {
            respond empresa
        } else {
            respond empresa.errors, status: 400
        }
    }

    def excluirEmpresa(int empresaId) {
        if (empresaService.empresaRepository.excluirEmpresa(empresaId)) {
            render status: 204
        } else {
            render status: 404, text: "Candidato não encontrado"
        }
    }
}
