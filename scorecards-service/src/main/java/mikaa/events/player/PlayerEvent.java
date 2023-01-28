package mikaa.events.player;

public record PlayerEvent(PlayerEventType type, PlayerPayload payload) {
}
