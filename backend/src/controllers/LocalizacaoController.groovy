package controllers

import model.Localizacao
import services.LocalizacaoService

class LocalizacaoController {
    private final LocalizacaoService localizacaoService

    LocalizacaoController(LocalizacaoService localizacaoService) {
        this.localizacaoService = localizacaoService
    }

    def listarTodasLocalizacoes() {
        try {
            respond localizacaoService.buscarTodasLocalizacoes()
        } catch (Exception e) {
            render status: 500, text: "Erro ao listar localizações: ${e.message}"
        }
    }

    def buscarLocalizacaoPorId(int localizacaoId) {
        try {
            def localizacao = localizacaoService.buscarLocalizacaoPorId(localizacaoId)
            if (localizacao) {
                respond localizacao
            } else {
                render status: 404, text: "Localização com ID ${localizacaoId} não encontrada"
            }
        } catch (Exception e) {
            render status: 500, text: "Erro ao buscar localização: ${e.message}"
        }
    }

    def cadastrarLocalizacao(Localizacao novaLocalizacao) {
        try {
            if (!novaLocalizacao) {
                render status: 400, text: "Dados da localização não fornecidos"
                return
            }

            def localizacaoCriada = localizacaoService.cadastrarLocalizacao(novaLocalizacao)
            respond localizacaoCriada, status: 201
        } catch (Exception e) {
            render status: 400, text: "Erro ao cadastrar localização: ${e.message}"
        }
    }

    def atualizarLocalizacao(Localizacao localizacaoAtualizada, int localizacaoId) {
        try {
            if (!localizacaoAtualizada) {
                render status: 400, text: "Dados da localização não fornecidos"
                return
            }

            boolean sucesso = localizacaoService.atualizarLocalizacao(localizacaoAtualizada, localizacaoId)
            if (sucesso) {
                respond localizacaoService.buscarLocalizacaoPorId(localizacaoId)
            } else {
                render status: 404, text: "Localização com ID ${localizacaoId} não encontrada"
            }
        } catch (Exception e) {
            render status: 400, text: "Erro ao atualizar localização: ${e.message}"
        }
    }
}