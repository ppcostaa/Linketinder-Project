package services

import infra.IUsuarioRepository
import model.Usuario

class UsuarioService {
    private final IUsuarioRepository usuarioRepository

    UsuarioService(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository
    }

    List<Usuario> buscarTodosUsuarios() {
        return usuarioRepository.buscarTodosUsuarios()
    }

    Usuario buscarUsuarioPorId(int usuarioId) {
        return usuarioRepository.buscarUsuarioPorId(usuarioId)
    }

    Usuario cadastrarUsuario(Usuario novoUsuario) {
        if (!novoUsuario.dadosValidos()) {
            throw new IllegalArgumentException("Dados de usuário incompletos ou inválidos")
        }

        if (usuarioRepository.emailExiste(novoUsuario.email)) {
            throw new IllegalArgumentException("Email já cadastrado no sistema")
        }

        return usuarioRepository.salvarUsuario(novoUsuario)
    }

    boolean atualizarUsuario(Usuario usuarioAtualizado) {
        if (!usuarioAtualizado.dadosValidos()) {
            throw new IllegalArgumentException("Dados de usuário incompletos ou inválidos")
        }

        Usuario usuarioExistente = usuarioRepository.buscarUsuarioPorId(usuarioAtualizado.usuarioId)
        if (!usuarioExistente) {
            return false
        }

        if (usuarioExistente.email != usuarioAtualizado.email &&
                usuarioRepository.emailExiste(usuarioAtualizado.email)) {
            throw new IllegalArgumentException("Email já cadastrado para outro usuário")
        }

        return usuarioRepository.atualizarUsuario(usuarioAtualizado)
    }

    boolean removerUsuario(int usuarioId) {
        return usuarioRepository.removerUsuario(usuarioId)
    }
}