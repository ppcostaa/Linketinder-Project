package controllers

import infra.CandidatoRepository
import model.Candidato
import services.CandidatoService

class CandidatoController {
    CandidatoService candidatoService

    def index() {
        respond candidatoService.candidatoRepository.listarCandidatos()
    }

    def listarCandidatoPorId(int candidatoId) {
        def user = candidatoService.candidatoRepository.listarCandidatoPorId(usuarioId)
        if (user) {
            respond user
        } else {
            render status: 404, text: "Candidato não encontrado"
        }
    }

    def salvarCandidato() {
        def candidato = new CandidatoRepository()
        if (candidato.salvarCandidato(flush: true)) {
            respond user, status: 201
        } else {
            respond user.errors, status: 400
        }
    }

    def editarCandidato(Candidato candidato) {
        def candidatos = candidatoService.candidatoRepository.listarCandidatoPorId(candidato)
        if (!candidatos) {
            render status: 404, text: "Candidato não encontrado"
            return
        }

        candidatoService.candidatoRepository.properties = params
        if (candidatoService.candidatoRepository.salvarCandidato(flush: true)) {
            respond user
        } else {
            respond user.errors, status: 400
        }
    }

    def excluirCandidato(int candidatoId) {
        if (candidatoService.candidatoRepository.excluirCandidato(usuarioId)) {
            render status: 204
        } else {
            render status: 404, text: "Candidato não encontrado"
        }
    }
}
