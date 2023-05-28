package mikaa.feature.scorecard;

import java.util.ArrayList;
import java.util.List;

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
  private List<Score> scores = new ArrayList<>();

  public Score addScore(Score score) {
    scores.add(score);
    return score;
  }

  public void removeScore(Score score) {
    scores.remove(score);
  }

}
