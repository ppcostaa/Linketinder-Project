package model

class Usuario {
    int usuarioId
    String email
    String senha
    String descricao

    Usuario() {}

    @Override
    String toString() {
        return "Usuario(id: $usuarioId, email: $email, descricao: $descricao)"
    }
}