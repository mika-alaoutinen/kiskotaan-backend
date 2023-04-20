package mikaa.events.scorecard;

public record ScoreCardEvent(ScoreCardEventType type, ScoreCardPayload payload) {
}
