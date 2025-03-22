//package model;
//
//class Empresa extends Usuario {
//    int idEmpresa
//    String cnpj
//    String nomeEmpresa
//    List<String> competencias
//
//
//    Empresa(int idUsuario, int idEmpresa, String nomeEmpresa, String email, String senha, String descricao, List<String> competencias, String cnpj, String pais, String cep) {
//        super(idUsuario, email, senha, descricao, pais, cep)
//        this.idEmpresa = idEmpresa
//        this.cnpj = cnpj
//        this.pais = pais
//        this.nomeEmpresa = nomeEmpresa
//        this.competencias = competencias
//    }
//
//}
package model

class Empresa {
    int idEmpresa
    int idUsuario
    int idLocalizacao
    String nomeEmpresa
    String cnpj
    List<Vaga> vagas = []
}
