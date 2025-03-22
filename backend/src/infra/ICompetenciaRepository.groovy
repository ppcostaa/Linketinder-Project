package infra

import model.Competencia

interface ICompetenciaRepository {
    List<Competencia> listarCompetencias()
}