package mikaa.logic;

import java.util.List;
import java.util.Map;

import lombok.Value;

@Value
public class ScoresByPlayer {

  private final Map<Long, PlayerScore> results;
  private final Map<Long, List<ScoreEntry>> scores;

}
