package controllers

import infra.LocalizacaoRepository
import model.Usuario
import services.LocalizacaoService

class LocalizacaoController {
    LocalizacaoService localizacaoService

    def index() {
        respond localizacaoService.localizacaoRepository.listarLocalizacoes()
    }

    def listarLocalizacaoPorId(int localizacaoId) {
        def localizacao = localizacaoService.localizacaoRepository.listarLocalizacaoPorId(usuarioId)
        if (localizacao) {
            respond localizacao
        } else {
            render status: 404, text: "Localização não encontrada"
        }
    }

    def salvarLocalizacao() {
        def localizacao = new LocalizacaoRepository()
        if (localizacao.salvarLocalizacao(flush: true)) {
            respond localizacao, status: 201
        } else {
            respond localizacao.errors, status: 400
        }
    }

    def editarLocalizacao(Usuario usuario) {
        def localizacoes = localizacaoService.localizacaoRepository.listarLocalizacaoPorId(localizacaoId)
        if (!localizacoes) {
            render status: 404, text: "Usuário não encontrado"
            return
        }

        localizacaoService.localizacaoRepository.properties = params
        if (localizacaoService.localizacaoRepository.salvarLocalizacao(flush: true)) {
            respond localizacoes
        } else {
            respond localizacoes.errors, status: 400
        }
    }
}
