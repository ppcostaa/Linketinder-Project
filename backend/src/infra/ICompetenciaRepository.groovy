package infra

import model.Competencia

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException


interface ICompetenciaRepository {
    List<Competencia> listarCompetencias(int candidatoId)

    Competencia listarCompetenciasPorId(int competenciaId)

    Competencia salvarCompetencia(Competencia competencia)
    Competencia competenciaPorNome(String nome)

    boolean excluirCompetencia(int competenciaId)

}