package mikaa.feature.score;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mikaa.feature.player.PlayerEntity;
import mikaa.feature.scorecard.ScoreCardEntity;

@Data
@EqualsAndHashCode(callSuper = false, exclude = { "player", "scorecard" })
@ToString(exclude = { "player", "scorecard" })
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "score")
public class ScoreEntity {

  @Id
  @GeneratedValue
  private Long id;

  @Min(1)
  @Max(30)
  private int hole;

  @Min(1)
  @Max(99)
  private int score;

  @ManyToOne(fetch = FetchType.LAZY)
  private PlayerEntity player;

  @ManyToOne(fetch = FetchType.LAZY)
  private ScoreCardEntity scorecard;

}
