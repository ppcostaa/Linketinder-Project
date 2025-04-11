package controllers


import model.Usuario
import services.UsuarioService

class UsuarioController {
    private final UsuarioService usuarioService

    UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService
    }

    def listarTodosUsuarios() {
        try {
            respond usuarioService.buscarTodosUsuarios()
        } catch (Exception e) {
            render status: 500, text: "Erro ao listar usuários: ${e.message}"
        }
    }

    def buscarUsuarioPorId(int usuarioId) {
        try {
            def usuario = usuarioService.buscarUsuarioPorId(usuarioId)
            if (usuario) {
                respond usuario
            } else {
                render status: 404, text: "Usuário com ID ${usuarioId} não encontrado"
            }
        } catch (Exception e) {
            render status: 500, text: "Erro ao buscar usuário: ${e.message}"
        }
    }

    def cadastrarUsuario(Usuario novoUsuario) {
        try {
            if (!novoUsuario) {
                render status: 400, text: "Dados do usuário não fornecidos"
                return
            }

            def usuarioCriado = usuarioService.cadastrarUsuario(novoUsuario)
            respond usuarioCriado, status: 201
        } catch (Exception e) {
            render status: 400, text: "Erro ao cadastrar usuário: ${e.message}"
        }
    }

    def atualizarUsuario(Usuario usuarioAtualizado) {
        try {
            if (!usuarioAtualizado || !usuarioAtualizado.usuarioId) {
                render status: 400, text: "Dados de usuário inválidos ou ID não fornecido"
                return
            }

            boolean sucesso = usuarioService.atualizarUsuario(usuarioAtualizado)
            if (sucesso) {
                respond usuarioService.buscarUsuarioPorId(usuarioAtualizado.usuarioId)
            } else {
                render status: 404, text: "Usuário com ID ${usuarioAtualizado.usuarioId} não encontrado"
            }
        } catch (Exception e) {
            render status: 400, text: "Erro ao atualizar usuário: ${e.message}"
        }
    }

    def removerUsuario(int usuarioId) {
        try {
            boolean sucesso = usuarioService.removerUsuario(usuarioId)
            if (sucesso) {
                render status: 204
            } else {
                render status: 404, text: "Usuário com ID ${usuarioId} não encontrado"
            }
        } catch (Exception e) {
            render status: 500, text: "Erro ao remover usuário: ${e.message}"
        }
    }
}