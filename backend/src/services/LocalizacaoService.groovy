package services

import infra.LocalizacaoRepository

class LocalizacaoService {
    final LocalizacaoRepository localizacaoRepository

    LocalizacaoService(LocalizacaoRepository localizacaoRepository) {
        this.localizacaoRepository = localizacaoRepository
    }
}
