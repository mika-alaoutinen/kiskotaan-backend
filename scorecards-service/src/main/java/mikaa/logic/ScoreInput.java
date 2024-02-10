package mikaa.logic;

import lombok.Value;

@Value
public class ScoreInput {
  private final long id;
  private final long playerId;
  private final int hole;
  private final int score;
}
