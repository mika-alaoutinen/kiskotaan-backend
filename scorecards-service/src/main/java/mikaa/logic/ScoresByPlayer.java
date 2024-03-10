package mikaa.logic;

import java.util.List;
import java.util.Map;

public record ScoresByPlayer(
    Map<Long, PlayerScore> results,
    Map<Long, List<ScoreEntry>> scores) {
}
