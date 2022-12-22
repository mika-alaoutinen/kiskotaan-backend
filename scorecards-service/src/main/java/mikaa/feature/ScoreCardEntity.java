package mikaa.feature;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "scorecard")
class ScoreCardEntity {

  @Id
  @GeneratedValue
  private Long id;

  @OneToOne(cascade = CascadeType.ALL, mappedBy = "scorecard")
  @PrimaryKeyJoinColumn
  private CourseEntity course;

  @Size(min = 1, max = 5, message = "Score card can have 1-5 players")
  @ElementCollection
  @CollectionTable(name = "players", joinColumns = @JoinColumn(name = "scorecard_id"))
  private List<Long> playerIds = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "scorecard", orphanRemoval = true)
  private List<ScoreEntity> scores = new ArrayList<>();

  void addScore(ScoreEntity score) {
    scores.add(score);
    score.setScorecard(this);
  }

  void removeScore(ScoreEntity score) {
    scores.remove(score);
    score.setScorecard(null);
  }

}
