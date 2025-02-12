class Empresa extends Pessoa {
    String cnpj
    String pais

    Empresa(String nome, String email, String estado, String cep, String descricao, List<String> competencias, String cnpj, String pais) {
        super(nome, email, estado, cep, descricao, competencias)
        this.cnpj = cnpj
        this.pais = pais
    }
    @Override
    String toString() {
        return """Empresa: $nome
Email: $email
Estado: $estado
CEP: $cep
Descrição: $descricao
Competências: ${competencias.join(", ")}
CNPJ: $cnpj
País: $pais
--------------------------------------"""
    }
}
