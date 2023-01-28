package mikaa.feature.player;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mikaa.feature.score.ScoreEntity;
import mikaa.feature.scorecard.ScoreCardEntity;

@Data
@EqualsAndHashCode(callSuper = false, exclude = { "scorecards", "scores" })
@ToString(exclude = { "scorecards", "scores" })
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "player")
public class PlayerEntity {

  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "player", orphanRemoval = true)
  private Set<ScoreEntity> scores = new HashSet<>();

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "players")
  private Set<ScoreCardEntity> scorecards = new HashSet<>();

  public PlayerEntity(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public PlayerEntity(Long id, String firstName, String lastName) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public void addScore(ScoreEntity score) {
    scores.add(score);
    score.setPlayer(this);
  }

  public void removeScore(ScoreEntity score) {
    scores.remove(score);
    score.setPlayer(null);
  }

}
