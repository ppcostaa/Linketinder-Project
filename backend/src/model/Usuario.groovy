package model

class Usuario {
    int idUsuario
    String email
    String senha
    String descricao

    Usuario() {}

    Usuario(Map<String, String> map) {
        this.idUsuario = map.idUsuario ? map.idUsuario.toInteger() : 0
        this.email = map.email
        this.senha = map.senha
        this.descricao = map.descricao
    }
}