package controllers

import infra.UsuarioRepository
import infra.VagaRepository
import model.Usuario
import model.Vaga
import services.UsuarioService
import services.VagaService

class VagaController {
    VagaService vagaService

    def index() {
        respond vagaService.vagaRepository.listarVagas()
    }

    def listarVagaPorId(int vagaId) {
        def vaga = vagaService.vagaRepository.listarVagaPorId(usuarioId)
        if (vaga) {
            respond vaga
        } else {
            render status: 404, text: "Usuário não encontrado"
        }
    }

    def salvarVaga() {
        def vaga = new VagaRepository()
        if (vaga.salvarVaga(flush: true)) {
            respond vaga, status: 201
        } else {
            respond vaga.errors, status: 400
        }
    }

    def editarVaga(Vaga vaga) {
        def vagas = vagaService.vagaRepository.listarVagaPorId(usuario)
        if (!vagas) {
            render status: 404, text: "Vaga não encontrada"
            return
        }

        vagaService.vagaRepository.properties = params
        if (vagaService.vagaRepository.salvarVaga(flush: true)) {
            respond vaga
        } else {
            respond vaga.errors, status: 400
        }
    }

    def excluirVaga(int vagaId) {
        if (vagaService.vagaRepository.excluirVaga(vagaId)) {
            render status: 204
        } else {
            render status: 404, text: "Vaga não encontrado"
        }
    }
}
