package mikaa.logic;

import java.util.Collection;

import lombok.Value;

@Value
class ScoreCardInput {
  private final long id;
  private final Collection<HoleInput> holes;
  private final Collection<ScoreInput> scores;
}
