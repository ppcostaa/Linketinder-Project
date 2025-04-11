package model

class Usuario {
    int usuarioId
    String email
    String senha
    String descricao

    Usuario() {}

    Usuario(int usuarioId, String email, String senha, String descricao) {
        this.usuarioId = usuarioId
        this.email = email
        this.senha = senha
        this.descricao = descricao
    }

    Usuario(String email, String senha, String descricao) {
        this.email = email
        this.senha = senha
        this.descricao = descricao
    }

    boolean dadosValidos() {
        return email?.trim() && senha?.trim()
    }

    @Override
    String toString() {
        return "Usuario(id: $usuarioId, email: $email, descricao: $descricao)"
    }
}