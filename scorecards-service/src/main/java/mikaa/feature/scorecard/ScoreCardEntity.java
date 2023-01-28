package mikaa.feature.scorecard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mikaa.feature.course.CourseEntity;
import mikaa.feature.player.PlayerEntity;
import mikaa.feature.score.ScoreEntity;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "scorecard")
public class ScoreCardEntity {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private CourseEntity course;

  @Size(min = 1, max = 5, message = "Score card can have 1-5 players")
  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "scorecard_player", joinColumns = @JoinColumn(name = "scorecard_id"), inverseJoinColumns = @JoinColumn(name = "player_id"))
  private Set<PlayerEntity> players = new HashSet<>();

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "scorecard", orphanRemoval = true)
  private List<ScoreEntity> scores = new ArrayList<>();

  ScoreCardEntity(CourseEntity course, Set<PlayerEntity> players) {
    this.course = course;
    this.players = players;
  }

  public void addPlayer(PlayerEntity player) {
    players.add(player);
    player.getScorecards().add(this);
  }

  public void removePlayer(PlayerEntity player) {
    players.remove(player);
    player.getScorecards().remove(this);
  }

  public void addScore(ScoreEntity score) {
    scores.add(score);
    score.setScorecard(this);
  }

  public void removeScore(ScoreEntity score) {
    scores.remove(score);
    score.setScorecard(null);
  }

}
