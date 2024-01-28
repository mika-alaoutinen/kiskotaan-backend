package mikaa.logic;

import java.util.Map;

import lombok.Value;

@Value
public class ScoresByHole {

  private final int par;
  private final Map<Long, Integer> scores;

}
