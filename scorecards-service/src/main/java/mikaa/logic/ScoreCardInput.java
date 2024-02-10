package mikaa.logic;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Value;

@Value
public class ScoreCardInput {
  private final long id;
  private final Collection<HoleInput> holes;
  private final Collection<ScoreInput> scores;

  Map<Integer, List<ScoreEntry>> groupScoresByHole() {
    return scoreEntries().collect(Collectors.groupingBy(ScoreEntry::getHole));
  }

  Map<Long, List<ScoreEntry>> groupScoresByPlayer() {
    return scoreEntries().collect(Collectors.groupingBy(ScoreEntry::getPlayerId));
  }

  private Stream<ScoreEntry> scoreEntries() {
    return scores.stream().map(s -> new ScoreEntry(s.getId(), s.getPlayerId(), s.getHole(), s.getScore()));
  }

}
