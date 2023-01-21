package mikaa.kafka.player;

public record PlayerEvent(PlayerEventType type, PlayerDTO payload) {
}
