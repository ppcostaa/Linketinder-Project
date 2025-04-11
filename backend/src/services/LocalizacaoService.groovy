package services

import infra.ILocalizacaoRepository
import model.Localizacao

class LocalizacaoService {
    private final ILocalizacaoRepository localizacaoRepository

    LocalizacaoService(ILocalizacaoRepository localizacaoRepository) {
        this.localizacaoRepository = localizacaoRepository
    }

    List<Localizacao> buscarTodasLocalizacoes() {
        return localizacaoRepository.buscarTodasLocalizacoes()
    }

    Localizacao buscarLocalizacaoPorId(int localizacaoId) {
        return localizacaoRepository.buscarLocalizacaoPorId(localizacaoId)
    }

    Localizacao cadastrarLocalizacao(Localizacao novaLocalizacao) {
        if (!novaLocalizacao.dadosValidos()) {
            throw new IllegalArgumentException("Dados de localização incompletos ou inválidos")
        }

        return localizacaoRepository.salvarLocalizacao(novaLocalizacao)
    }

    boolean atualizarLocalizacao(Localizacao localizacaoAtualizada, int localizacaoId) {
        if (!localizacaoAtualizada.dadosValidos()) {
            throw new IllegalArgumentException("Dados de localização incompletos ou inválidos")
        }

        Localizacao localizacaoExistente = localizacaoRepository.buscarLocalizacaoPorId(localizacaoId)
        if (!localizacaoExistente) {
            return false
        }

        return localizacaoRepository.atualizarLocalizacao(localizacaoAtualizada, localizacaoId)
    }
}