package mikaa.events.score;

public record ScorePayload(
    Long id,
    int hole,
    int score,
    long playerId,
    long scoreCardId) {
}
