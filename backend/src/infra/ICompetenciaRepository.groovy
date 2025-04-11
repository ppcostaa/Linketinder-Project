package infra

import model.Competencia

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException


interface ICompetenciaRepository {
    List<Competencia> listarCompetenciasPorCandidato(int candidatoId)

    Competencia listarCompetenciasPorId(int competenciaId)

    Competencia salvarCompetencia(Competencia competencia)
    boolean editarCompetencia(Competencia competencia)

    boolean excluirCompetencia(int competenciaId)

}