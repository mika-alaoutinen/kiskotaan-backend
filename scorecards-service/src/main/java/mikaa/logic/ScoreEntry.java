package mikaa.logic;

import lombok.Value;

@Value
public class ScoreEntry {

  private final long id;
  private final long playerId;
  private final int hole;
  private final int score;

}
