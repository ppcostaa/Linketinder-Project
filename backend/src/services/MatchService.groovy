package services

import infra.MatchRepository

class MatchService {
    final MatchRepository matchRepository

    MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository
    }
}