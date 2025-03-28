package controllers

import model.Match
import model.Like

class MatchController {
    List<Match> matches = []

    void verificarMatches(LikeController likeController) {
        List<Like> likes = likeController.listarLikes()

        likes.each { likeCandidato ->
            if (likeCandidato.vagaId != 0) {
                likes.each { likeEmpresa ->
                    if (likeEmpresa.empresaId != 0 && likeEmpresa.candidatoId == likeCandidato.candidatoId) {
                        Match match = new Match(matches.size() + 1, likeCandidato.candidatoId, likeEmpresa.empresaId, likeCandidato.vagaId)
                        matches.add(match)
                        println "Match encontrado entre candidato ${likeCandidato.candidatoId} e empresa ${likeEmpresa.empresaId} para a vaga ${likeCandidato.vagaId}"
                    }
                }
            }
        }
    }

    List<Match> listarMatches() {
        return matches
    }
}