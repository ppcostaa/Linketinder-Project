package model

class Usuario {
    final int usuarioId
    String email
    String senha
    String descricao

    Usuario(int usuarioId, String email, String senha, String descricao) {
        this.usuarioId = usuarioId
        this.email = email
        this.senha = senha
        this.descricao = descricao
    }
}