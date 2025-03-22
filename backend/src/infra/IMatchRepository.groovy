package infra

import model.Match

public interface IMatchRepository {
    void salvarMatch(Match match);
    boolean verificarMatch(int candidatoId, int empresaId)
}