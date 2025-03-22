package infra

import model.Match

interface IMatchRepository {
    void salvarMatch(Match match);

    boolean verificarMatch(int candidatoId, int empresaId)
}