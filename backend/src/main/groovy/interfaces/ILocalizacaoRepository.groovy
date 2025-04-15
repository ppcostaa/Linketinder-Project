package interfaces

import model.Localizacao


interface ILocalizacaoRepository {
    Localizacao listarLocalizacoesPorId(int localizacaoId)

    boolean editarLocalizacao(Localizacao localizacao, int localizacaoId)
}