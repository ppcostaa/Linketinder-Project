package infra

import model.Usuario

interface IUsuarioRepository {
    boolean emailExiste(String email)

    Usuario salvarUsuario(Usuario usuario)

    Usuario buscarUsuarioPorId(int usuarioId)

    List<Usuario> buscarTodosUsuarios()

    boolean atualizarUsuario(Usuario usuario)

    boolean removerUsuario(int usuarioId)
}