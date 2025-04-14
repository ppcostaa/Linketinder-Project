package groovy.infra

import groovy.model.Competencia

interface ICompetenciaRepository {
    List<Competencia> listarCompetenciasPorCandidato(int candidatoId)

    Competencia listarCompetenciasPorId(int competenciaId)

    Competencia salvarCompetencia(Competencia competencia)

    boolean editarCompetencia(Competencia competencia)

    boolean excluirCompetencia(int competenciaId)

}