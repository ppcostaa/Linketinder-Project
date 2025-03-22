package model;

class Empresa extends Usuario {
    final int empresaId
    String cnpj
    String empresaNome
    List<String> competencias = []
    final int localizacaoId


    Empresa(int usuarioId, int empresaId, int localizacaoId, String empresaNome, String email, String senha, String descricao, List<String> competencias, String cnpj, String pais, String cep) {
        super(usuarioId, email, senha, descricao, pais, cep)
        this.empresaId = empresaId
        this.cnpj = cnpj
        this.empresaNome = empresaNome
        this.competencias = competencias
        this.localizacaoId = localizacaoId
    }

}
