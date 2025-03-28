package services

import infra.UsuarioRepository

class UsuarioService {
    final UsuarioRepository usuarioRepository

    UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository
    }
}
