package interfaces

import model.Localizacao


interface ILocalizacaoDAO {
    Localizacao listarLocalizacoesPorId(int localizacaoId)

    boolean editarLocalizacao(Localizacao localizacao, int localizacaoId)
}