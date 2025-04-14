package groovy.infra

import groovy.model.Localizacao


interface ILocalizacaoRepository {
    Localizacao salvarLocalizacao(Localizacao localizacao)

    List<Localizacao> listarLocalizacoes()

    Localizacao listarLocalizacoesPorId(int localizacaoId)

    boolean editarLocalizacao(Localizacao localizacao, int localizacaoId)
}