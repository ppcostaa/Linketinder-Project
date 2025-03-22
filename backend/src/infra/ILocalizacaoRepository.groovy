package infra

import model.Localizacao

interface ILocalizacaoRepository {
    Localizacao salvarLocalizacao(Localizacao localizacao)

    List<Localizacao> listarLocalizacoes()

    Localizacao listarLocalizacaoPorId(int localizacaoId)

    boolean editarLocalizacao(Localizacao localizacao, int candidatoId)


}