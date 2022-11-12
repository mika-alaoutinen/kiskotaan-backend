package mikaa.players.events;

import java.util.Objects;

public record Player(long id, String firstName, String lastName) {
  public Player {
    Objects.requireNonNull(id);
    Objects.requireNonNull(firstName);
    Objects.requireNonNull(lastName);
  }
}
