package mikaa.feature.scorecard;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mikaa.feature.course.CourseEntity;
import mikaa.feature.player.PlayerEntity;

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

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "scorecard", optional = false)
  private CourseEntity course;

  @Size(min = 1, max = 5, message = "Score card can have 1-5 players")
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "scorecard", orphanRemoval = true)
  private List<PlayerEntity> players = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "scorecard", orphanRemoval = true)
  private List<ScoreEntity> scores = new ArrayList<>();

  ScoreCardEntity(CourseEntity course, List<PlayerEntity> players) {
    this.course = course;
    this.players = players;
  }

  void setCourse(CourseEntity course) {
    this.course = course;
    course.setScorecard(this);
  }

  void addPlayer(PlayerEntity player) {
    players.add(player);
    player.setScorecard(this);
  }

  void removePlayer(PlayerEntity player) {
    players.remove(player);
    player.setScorecard(null);
  }

  void addScore(ScoreEntity score) {
    scores.add(score);
    score.setScorecard(this);
  }

  void removeScore(ScoreEntity score) {
    scores.remove(score);
    score.setScorecard(null);
  }

}