package mikaa.feature.results;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import mikaa.domain.Hole;
import mikaa.domain.Score;
import mikaa.domain.ScoreCard;

public record ScoreCardInput(long id, Collection<ScoreEntry> scores) {

  public static ScoreCardInput from(ScoreCard scoreCard) {
    var holePars = scoreCard.course()
        .holes()
        .stream()
        .collect(Collectors.toMap(Hole::number, Hole::par));

    var scores = scoreCard.scores()
        .stream()
        .map(s -> toScoreEntry(s, holePars))
        .toList();

    return new ScoreCardInput(scoreCard.id(), scores);
  }

  private static ScoreEntry toScoreEntry(Score score, Map<Integer, Integer> holePars) {
    int par = holePars.getOrDefault(score.hole(), 0);
    return new ScoreEntry(score.id(), score.playerId(), score.hole(), par, score.score());
  }

}
