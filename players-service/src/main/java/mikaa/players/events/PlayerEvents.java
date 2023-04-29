package mikaa.players.events;

public interface PlayerEvents {

  public record PlayerEvent(EventType type, PlayerPayload payload) {
  }

  enum EventType {
    PLAYER_ADDED, PLAYER_DELETED, PLAYER_UPDATED
  }

  public static PlayerEvent add(PlayerPayload player) {
    return new PlayerEvent(EventType.PLAYER_ADDED, player);
  }

  public static PlayerEvent update(PlayerPayload player) {
    return new PlayerEvent(EventType.PLAYER_UPDATED, player);
  }

  public static PlayerEvent delete(PlayerPayload player) {
    return new PlayerEvent(EventType.PLAYER_DELETED, player);
  }

}
