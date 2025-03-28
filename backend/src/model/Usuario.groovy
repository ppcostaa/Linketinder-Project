package model

class Usuario {
    int usuarioId
    String email
    String senha
    String descricao

    Usuario(){}
    Usuario(int usuarioId, String email, String senha, String descricao) {
        this.usuarioId = usuarioId
        this.email = email
        this.senha = senha
        this.descricao = descricao
    }
}