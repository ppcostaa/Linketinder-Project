package controllers

import infra.IMatchRepository
import infra.MatchRepository
import model.Match

class MatchController {
    MatchRepository matchRepository

    MatchController(MatchRepository matchRepository) {
        this.matchRepository = matchRepository
    }

    def index() {
        respond matchRepository.listarMatchs()
    }

    def listarMatchs() {
        def user = matchRepository.listarMatchs()
        if (user) {
            respond user
        } else {
            render status: 404, text: "Match não encontrado"
        }
    }

    def salvarMatch() {
        def match = new IMatchRepository(params) {
            @Override
            void salvarMatch(Match match) {

            }

            @Override
            boolean verificarMatch(int candidatoId, int empresaId) {
                return false
            }
        }
        if (match.salvarMatch(flush: true)) {
            respond match, status: 201
        } else {
            respond match.errors, status: 400
        }
    }

}
