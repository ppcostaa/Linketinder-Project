package interfaces

import model.Competencia


interface ICompetenciaDAO {
    List<Competencia> listarCompetenciasPorCandidato(int candidatoId)

    Competencia listarCompetenciasPorId(int competenciaId)

    Competencia salvarCompetencia(Competencia competencia)

    boolean editarCompetencia(Competencia competencia)

    boolean excluirCompetencia(int competenciaId)
}