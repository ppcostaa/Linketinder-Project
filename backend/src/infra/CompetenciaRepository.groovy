package infra

import database.ConnectionFactory
import model.Competencia

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class CompetenciaRepository implements ICompetenciaRepository {
    ConnectionFactory connectionFactory = new ConnectionFactory(
            'jdbc:postgresql://localhost:5432/linketinder', 'postgres', 'senha123'
    )

    @Override
    List<Competencia> listarCompetencias() {
        String sql = "SELECT * FROM COMPETENCIAS"
        List<Competencia> competencias = []

        try (Connection conn = connectionFactory.createConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Competencia competencia = new Competencia(
                        rs.getInt("ID_COMPETENCIA"),
                        rs.getString("NOME_COMPETENCIA")
                )
                competencias.add(competencia)
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar competências: " + e.getMessage(), e)
        }

        return competencias
    }

}