package interfaces

import model.Usuario

interface IUsuarioRepository {
    boolean emailExiste(String email)

    Usuario listarUsuarioPorId(int usuarioId)

    boolean editarUsuario(Usuario usuario)
}