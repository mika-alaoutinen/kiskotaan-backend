package mikaa.players.events;

import java.util.Objects;

public record PlayerAdded(Player player) {

  public static String type = "PLAYER_ADDED";

  public PlayerAdded {
    Objects.requireNonNull(player);
  }
}
