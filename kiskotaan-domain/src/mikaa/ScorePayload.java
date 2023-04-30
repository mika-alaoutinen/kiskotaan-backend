package mikaa;

public record ScorePayload(Long id, int hole, int score, long playerId, long scoreCardId) {
}
