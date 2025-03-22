//package utils
//
//import model.Empresa
//import model.Vaga
//
//class GerenciadorVaga {
//    def cadastrarVaga(Empresa empresa, Vaga vaga) {
//        sql.executeInsert("INSERT INTO VAGAS (ID_EMPRESA, NOME_VAGA, DESCRICAO_VAGA, LOCAL_ESTADO, LOCAL_CIDADE) VALUES (?, ?, ?, ?, ?)", [
//                empresa.idUsuario, vaga.nomeVaga, vaga.descricaoVaga, vaga.localEstado, vaga.localCidade
//        ])
//        println "✅ Vaga cadastrada com sucesso!"
//    }
//    def listarVagas(Empresa empresa) {
//        def sql = Conexao.obterConexao()
//
//        if (sql) {
//            try {
//                def vagas = sql.rows("SELECT * FROM VAGAS WHERE ID_EMPRESA = ?", [empresa.idUsuario])
//                if (vagas.size() > 0) {
//                    println "===== VAGAS DA EMPRESA ${empresa.nome} ====="
//                    vagas.each { vaga ->
//                        println "Vaga: ${vaga.NOME_VAGA}"
//                        println "Descrição: ${vaga.DESCRICAO_VAGA}"
//                        println "Localização: ${vaga.LOCAL_CIDADE} - ${vaga.LOCAL_ESTADO}"
//                        println "--------------------------------------"
//                    }
//                } else {
//                    println "Nenhuma vaga cadastrada para a empresa ${empresa.nome}."
//                }
//            } catch (Exception e) {
//                println "❌ Erro ao listar as vagas: ${e.message}"
//            } finally {
//                sql.close()
//            }
//        } else {
//            println "❌ Conexão não foi estabelecida."
//        }
//    }
//}
package utils

import model.Vaga
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement


class GerenciadorVaga {

    Vaga create(Vaga vaga) {
        String sql = "INSERT INTO VAGAS (ID_EMPRESA, NOME_VAGA, DESCRICAO_VAGA, LOCAL_ESTADO, LOCAL_CIDADE) VALUES (?, ?, ?, ?, ?) RETURNING ID_VAGA"

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/linketinder",
                "postgres",
                "senha123")
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, vaga.idEmpresa)
            stmt.setString(2, vaga.nomeVaga)
            stmt.setString(3, vaga.descricaoVaga)
            stmt.setString(4, vaga.localEstado)
            stmt.setString(5, vaga.localCidade)

            int affectedRows = stmt.executeUpdate()

            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys()
                if (rs.next()) {
                    vaga.idVaga = rs.getInt(1)
                }
                rs.close()
            }

            return vaga
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar vaga: " + e.getMessage(), e)
        }
    }

    Vaga findById(Integer id) {
        String sql = "SELECT * FROM VAGAS WHERE ID_VAGA = ?"

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/linketinder",
                "postgres",
                "senha123")
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id)
            ResultSet rs = stmt.executeQuery()

            if (rs.next()) {
                Vaga vaga = new Vaga()
                vaga.idVaga = rs.getInt("ID_VAGA")
                vaga.idEmpresa = rs.getInt("ID_EMPRESA")
                vaga.nomeVaga = rs.getString("NOME_VAGA")
                vaga.descricaoVaga = rs.getString("DESCRICAO_VAGA")
                vaga.localEstado = rs.getString("LOCAL_ESTADO")
                vaga.localCidade = rs.getString("LOCAL_CIDADE")
                return vaga
            }

            return null
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar vaga: " + e.getMessage(), e)
        }
    }

    List<Vaga> findAll() {
        String sql = "SELECT * FROM VAGAS"
        List<Vaga> vagas = []

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/linketinder",
                "postgres",
                "senha123")
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Vaga vaga = new Vaga()
                vaga.idVaga = rs.getInt("ID_VAGA")
                vaga.idEmpresa = rs.getInt("ID_EMPRESA")
                vaga.nomeVaga = rs.getString("NOME_VAGA")
                vaga.descricaoVaga = rs.getString("DESCRICAO_VAGA")
                vaga.localEstado = rs.getString("LOCAL_ESTADO")
                vaga.localCidade = rs.getString("LOCAL_CIDADE")
                vagas.add(vaga)
            }

            return vagas
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar vagas: " + e.getMessage(), e)
        }
    }

    boolean update(Vaga vaga) {
        String sql = "UPDATE VAGAS SET NOME_VAGA = ?, DESCRICAO_VAGA = ?, LOCAL_ESTADO = ?, LOCAL_CIDADE = ? WHERE ID_VAGA = ?"

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/linketinder",
                "postgres",
                "senha123")
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vaga.nomeVaga)
            stmt.setString(2, vaga.descricaoVaga)
            stmt.setString(3, vaga.localEstado)
            stmt.setString(4, vaga.localCidade)
            stmt.setInt(5, vaga.idVaga)

            int affectedRows = stmt.executeUpdate()
            return affectedRows > 0
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar vaga: " + e.getMessage(), e)
        }
    }

    boolean delete(Integer id) {
        String sql = "DELETE FROM VAGAS WHERE ID_VAGA = ?"

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/linketinder",
                "postgres",
                "senha123")
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id)

            int affectedRows = stmt.executeUpdate()
            return affectedRows > 0
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir vaga: " + e.getMessage(), e)
        }
    }
}