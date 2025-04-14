package groovy.model;

class Empresa extends Usuario {
    int empresaId
    String cnpj
    String empresaNome
    List<String> competencias = []
    int localizacaoId

    Empresa() {}

    Empresa(int usuarioId, int empresaId, int localizacaoId, String empresaNome, String email, String senha, String descricao, List<String> competencias, String cnpj, String pais, String cep) {
        super(usuarioId, email, senha, descricao, pais, cep)
        this.empresaId = empresaId
        this.cnpj = cnpj
        this.empresaNome = empresaNome
        this.competencias = competencias
        this.localizacaoId = localizacaoId
    }

}
