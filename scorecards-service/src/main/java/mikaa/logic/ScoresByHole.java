package mikaa.logic;

import java.util.List;
import java.util.Map;

public record ScoresByHole(
    Map<Long, PlayerScore> results,
    Map<Integer, List<ScoreEntry>> scores) {
}
