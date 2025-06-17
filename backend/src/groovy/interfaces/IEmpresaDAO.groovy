package interfaces

import model.Empresa


interface IEmpresaDAO {
    Empresa salvarEmpresa(Empresa empresa, String email, String senha, String descricao, String cep, String pais)

    Empresa listarEmpresaPorId(int empresaId)

    List<Empresa> listarEmpresas()

    boolean editarEmpresa(Empresa empresa)

    boolean excluirEmpresa(int empresaId)


}