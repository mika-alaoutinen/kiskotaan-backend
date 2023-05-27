package mikaa.feature.scorecard;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Player {

  private long externalId;
  private String firstName;
  private String lastName;
  private Set<Score> scores;

  public Score addScore(Score score) {
    scores.add(score);
    return score;
  }

  public void removeScore(Score score) {
    scores.remove(score);
  }

}
