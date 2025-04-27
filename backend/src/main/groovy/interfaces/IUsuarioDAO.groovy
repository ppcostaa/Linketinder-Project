package interfaces

import model.Usuario

interface IUsuarioDAO {
    boolean emailExiste(String email)

    Usuario listarUsuarioPorId(int usuarioId)

    boolean editarUsuario(Usuario usuario)
}