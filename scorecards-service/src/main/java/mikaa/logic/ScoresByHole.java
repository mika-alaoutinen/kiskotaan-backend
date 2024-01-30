package mikaa.logic;

import java.util.Map;

import lombok.Value;

@Value
public class ScoresByHole {

  private final Map<Long, PlayerScore> results;
  private final Map<Integer, Map<Long, Integer>> scores;

}
