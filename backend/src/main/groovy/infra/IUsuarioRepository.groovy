package groovy.infra

import groovy.model.Usuario

interface IUsuarioRepository {
    boolean emailExiste(String email)

    Usuario salvarUsuario(Usuario usuario)

    Usuario listarUsuarioPorId(int usuarioId)

    List<Usuario> listarUsuarios()

    boolean editarUsuario(Usuario usuario)

    boolean excluirUsuario(int usuarioId)
}