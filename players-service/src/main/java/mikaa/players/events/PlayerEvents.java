package mikaa.players.events;

public interface PlayerEvents {

  public record PlayerEvent(EventType type, Player player) {
  }

  enum EventType {
    PLAYER_ADDED, PLAYER_DELETED, PLAYER_UPDATED
  }

  public static PlayerEvent add(Player player) {
    return new PlayerEvent(EventType.PLAYER_ADDED, player);
  }

  public static PlayerEvent update(Player player) {
    return new PlayerEvent(EventType.PLAYER_UPDATED, player);
  }

  public static PlayerEvent delete(Player player) {
    return new PlayerEvent(EventType.PLAYER_DELETED, player);
  }

}
