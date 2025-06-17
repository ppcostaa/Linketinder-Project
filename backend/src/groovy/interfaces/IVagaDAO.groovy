package interfaces


import model.Vaga

interface IVagaDAO {
    Vaga salvarVaga(Vaga vaga)

    Vaga listarVagaPorId(int vagaId)

    List<Vaga> listarVagas()

    boolean editarVaga(Vaga vaga)

    boolean excluirVaga(int vagaId)

}