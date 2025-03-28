package controllers

import infra.UsuarioRepository
import model.Usuario
import services.UsuarioService

class UsuarioController {
    UsuarioService usuarioService

    def index() {
        respond usuarioService.usuarioRepository.listarUsuarios()
    }

    def listarUsuariosPorId(int usuarioId) {
        def user = usuarioService.usuarioRepository.listarUsuariosPorId(usuarioId)
        if (user) {
            respond user
        } else {
            render status: 404, text: "Usuário não encontrado"
        }
    }

    def salvarUsuario() {
        def usuario = new UsuarioRepository()
        if (usuario.salvarUsuario(flush: true)) {
            respond user, status: 201
        } else {
            respond user.errors, status: 400
        }
    }

    def editarUsuario(Usuario usuario) {
        def usuarios = usuarioService.usuarioRepository.listarUsuariosPorId(usuario)
        if (!usuarios) {
            render status: 404, text: "Usuário não encontrado"
            return
        }

        usuarioService.usuarioRepository.properties = params
        if (usuarioService.usuarioRepository.salvarUsuario(flush: true)) {
            respond user
        } else {
            respond user.errors, status: 400
        }
    }

    def excluirUsuario(int usuarioId) {
        if (usuarioService.usuarioRepository.excluirUsuario(usuarioId)) {
            render status: 204
        } else {
            render status: 404, text: "Usuário não encontrado"
        }
    }
}

