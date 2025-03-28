package controllers

import model.Like

class LikeController {
    List<Like> likes = []

    void darLikeCandidato(int likeId, int candidatoId, int vagaId) {
        Like like = new Like(likeId, candidatoId, vagaId, 0)
        likes.add(like)
    }

    void darLikeEmpresa(int likeId, int empresaId, int candidatoId) {
        Like like = new Like(likeId, candidatoId, 0, empresaId)
        likes.add(like)
    }

    List<Like> listarLikes() {
        return likes
    }
}