package infra

import model.Competencia

interface ICompetenciaRepository {
    Competencia salvarCompetencia(Competencia competencia)

    List<Competencia> listarCompetencias()
}