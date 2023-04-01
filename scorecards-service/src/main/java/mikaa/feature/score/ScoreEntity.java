package mikaa.feature.score;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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

  ScoreEntity(int hole, int score) {
    this.hole = hole;
    this.score = score;
  }

}
