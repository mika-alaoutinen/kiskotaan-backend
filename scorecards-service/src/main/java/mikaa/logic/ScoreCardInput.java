package mikaa.logic;

import java.util.Collection;
import java.util.List;

import lombok.Value;
import mikaa.feature.scorecard.ScoreCardEntity;
import mikaa.kiskotaan.scorecard.ScoreCardPayload;

@Value
public class ScoreCardInput {
  private final long id;
  private final Collection<HoleInput> holes;
  private final Collection<ScoreInput> scores;

  public static ScoreCardInput from(ScoreCardEntity entity) {
    var holes = entity.getCourse()
        .getHoles()
        .stream()
        .map(h -> new HoleInput(h.getNumber(), h.getPar()))
        .toList();

    var scores = entity.getScores()
        .stream()
        .map(s -> new ScoreInput(s.getId(), s.getPlayer().getExternalId(), s.getHole(), s.getScore()))
        .toList();

    return new ScoreCardInput(entity.getId(), holes, scores);
  }

  public static ScoreCardInput from(ScoreCardPayload payload) {
    // This is not going to work. Need to add full course info to ScoreCardPayload?
    // Alternatively, add par to each score entry?
    List<HoleInput> holes = List.of();
    return new ScoreCardInput(payload.getId(), holes, null);
  }

}
