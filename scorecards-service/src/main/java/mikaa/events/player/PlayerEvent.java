package mikaa.events.player;

public record PlayerEvent(PlayerEventType type, PlayerDTO payload) {
}
