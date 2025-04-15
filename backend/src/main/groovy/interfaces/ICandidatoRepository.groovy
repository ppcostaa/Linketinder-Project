package interfaces

import model.Candidato


interface ICandidatoRepository {
    Candidato salvarCandidato(Candidato candidato, String email, String senha, String descricao, String cep, String pais)

    Candidato listarCandidatoPorId(int id)

    boolean editarCandidato(Candidato candidato)

    boolean excluirCandidato(int id)

}