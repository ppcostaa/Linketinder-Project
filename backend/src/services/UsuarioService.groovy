package services

import infra.UsuarioRepository
import model.Usuario

class UsuarioService {
    final UsuarioRepository usuarioRepository

    UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository
    }

    List<Usuario> buscarTodosUsuarios() {
        return usuarioRepository.listarUsuarios()
    }

    Usuario buscarUsuarioPorId(int usuarioId) {
        return usuarioRepository.listarUsuariosPorId(usuarioId)
    }

    Usuario cadastrarUsuario(Usuario usuario) {
        if (usuarioRepository.emailExiste(usuario.email)) {
            throw new RuntimeException("Email já cadastrado")
        }
        return usuarioRepository.salvarUsuario(usuario)
    }

    boolean atualizarUsuario(Usuario usuario) {
        return usuarioRepository.editarUsuario(usuario)
    }

    boolean removerUsuario(int usuarioId) {
        return usuarioRepository.excluirUsuario(usuarioId)
    }
}