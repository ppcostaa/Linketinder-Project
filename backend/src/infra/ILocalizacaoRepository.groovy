package infra

import model.Localizacao

interface ILocalizacaoRepository {
    Localizacao salvarLocalizacao(Localizacao localizacao)

    List<Localizacao> buscarTodasLocalizacoes()

    Localizacao buscarLocalizacaoPorId(int localizacaoId)

    boolean atualizarLocalizacao(Localizacao localizacao, int localizacaoId)
}