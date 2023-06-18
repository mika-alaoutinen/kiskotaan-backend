package mikaa.logic;

import java.util.List;

import lombok.Value;

@Value
public class PlayerScore {

  private final List<ScoreEntry> entries;
  private final int result;
  private final int total;

}
