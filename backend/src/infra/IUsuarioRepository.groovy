package infra

import model.Usuario

interface IUsuarioRepository {
    boolean emailExiste(String email)

    Usuario salvarUsuario(Usuario usuario);

    Usuario listarUsuariosPorId(int usuarioId)

    List<Usuario> listarUsuarios()

    boolean editarUsuario(Usuario usuario)

    boolean excluirUsuario(int usuarioId)
}