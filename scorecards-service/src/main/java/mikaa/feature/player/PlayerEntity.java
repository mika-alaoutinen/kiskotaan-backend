package mikaa.feature.player;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter(value = AccessLevel.PACKAGE)
  @Setter(value = AccessLevel.PACKAGE)
  private Long id;

  @Column(name = "external_id", unique = true)
  private long externalId;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "player", orphanRemoval = true)
  private Set<ScoreEntity> scores = new HashSet<>();

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "players")
  private Set<ScoreCardEntity> scorecards = new HashSet<>();

  public PlayerEntity(long externalId, String firstName, String lastName) {
    this.externalId = externalId;
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

  public void removeFromScoreCards() {
    scorecards.forEach(sc -> sc.removePlayer(this));
  }

}
